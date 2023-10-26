package com.hfad.whatsappclone.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.hfad.whatsappclone.data.localDatabase.UserDao
import com.hfad.whatsappclone.domain.model.ModelChat
import com.hfad.whatsappclone.domain.model.ModelMessage
import com.hfad.whatsappclone.domain.model.ModelUser
import com.hfad.whatsappclone.domain.repository.UserRepository
import com.hfad.whatsappclone.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore,
    protected val userDao: UserDao
) : UserRepository {

    override fun getAllUsers(deviceContacts: List<String>): Flow<Resource<List<ModelUser>>> =
        channelFlow {
            try {
                trySend(Resource.Loading)
                val contacts = userDao.getAllUsers().firstOrNull()
                if (contacts.isNullOrEmpty()) {
                    for (contact in deviceContacts) {
                        val query =
                            firestore.collection("users").whereEqualTo("userNumber", contact)
                        query.get().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                for (document in task.result) {
                                    val user = getUserFromDocument(document)
                                    CoroutineScope(Dispatchers.IO).launch {
                                        userDao.insertUser(user)
                                        userDao.getAllUsers().collectLatest {
                                            trySend(Resource.Success(it))
                                        }
                                    }
                                }
                            }
                        }
                    }
                    awaitClose()
                } else {
                    trySend(Resource.Success(contacts))
                }
            } catch (exception: Exception) {
                trySend(Resource.Error(exception.localizedMessage ?: "An Error Occurred"))
            }
        }

    override fun getAllChats(userId: String): Flow<Resource<List<ModelChat>>> = callbackFlow {
        try {
            trySend(Resource.Loading)
            val query = firestore.collection("chats").whereArrayContains("chatParticipants", userId)
            val listener = query.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    trySend(Resource.Error(exception.localizedMessage ?: "An Error Occurred"))
                    return@addSnapshotListener
                }
                snapshot?.let { documents ->
                    val chats = mutableListOf<ModelChat>()
                    for (document in documents) {
                        val chat = getChatFromDocument(document)
                        chats.add(chat)
                    }
                    trySend(Resource.Success(chats))
                }
            }
            awaitClose {
                listener.remove()
            }

        } catch (exception: Exception) {
            trySend(Resource.Error(exception.localizedMessage ?: "An Error Occurred"))
        }
    }

    override fun getAllMessagesOfChat(chatId: String): Flow<Resource<List<ModelMessage>>> =
        callbackFlow {
            try {
                trySend(Resource.Loading)
                val listener = firestore.collection("chats").document(chatId).collection("messages")
                    .addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            trySend(
                                Resource.Error(
                                    exception.localizedMessage ?: "An Error Occurred"
                                )
                            )
                            return@addSnapshotListener
                        }
                        snapshot?.let { documents ->
                            val messages = mutableListOf<ModelMessage>()
                            for (document in documents) {
                                val messageModel = getMessageFromDocument(document)
                                messages.add(messageModel)
                            }
                            trySend(Resource.Success(messages))
                        }
                    }
                awaitClose {
                    listener.remove()
                }
            } catch (exception: Exception) {
                trySend(Resource.Error(exception.localizedMessage ?: "An Error Occurred"))
            }
        }.catch {
            Log.d("ErrorFromSendingMessage", it.localizedMessage ?: "Callback Flow Exception")
        }

    override fun sendMessage(chatId: String, messageModel: ModelMessage): Flow<Resource<Boolean>> =
        callbackFlow {
            try {
                trySend(Resource.Loading)
                firestore.collection("chats")
                    .document(chatId)
                    .collection("messages")
                    .document()
                    .set(messageModel)
                    .addOnSuccessListener {
                        trySend(Resource.Success<Boolean>(true))
                    }
                    .addOnFailureListener {
                        trySend(Resource.Error("Message Sending Failed due to ${it.localizedMessage}"))
                    }
                awaitClose()
            } catch (e: Exception) {
                trySend(Resource.Error("Message Sending Failed due to ${e.localizedMessage}"))
            }
        }

    private fun getChatFromDocument(document: QueryDocumentSnapshot): ModelChat {
        return ModelChat(
            chatId = document.id,
            chatParticipants = document.get("chatParticipants") as List<String>,
            chatImage = document.get("chatImage") as String,
            chatName = document.get("chatName") as String,
            chatLastMessage = document.get("chatLastMessage") as String,
            chatLastMessageTimestamp = document.get("chatLastMessageTimestamp") as String,
        )

    }

    private fun getUserFromDocument(document: QueryDocumentSnapshot): ModelUser {
        return ModelUser(
            userNumber = document.getString("userNumber"),
            userName = document.getString("userName"),
            userImage = document.getString("userImage"),
            userStatus = document.getString("userStatus"),
            userId = document.id
        )
    }

    private fun getMessageFromDocument(document: QueryDocumentSnapshot): ModelMessage {
        return ModelMessage(
            messageData = document.get("messageData").toString(),
            messageType = document.get("messageType").toString(),
            messageReceiver = document.get("messageReceiver").toString(),
            messageSender = document.get("messageSender").toString()
        )
    }
}