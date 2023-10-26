package com.hfad.whatsappclone.domain.repository

import com.hfad.whatsappclone.domain.model.ModelChat
import com.hfad.whatsappclone.domain.model.ModelMessage
import com.hfad.whatsappclone.domain.model.ModelUser
import com.hfad.whatsappclone.util.Resource
import kotlinx.coroutines.flow.Flow


interface UserRepository {
    fun getAllUsers(deviceContacts: List<String>): Flow<Resource<List<ModelUser>>>

    fun getAllChats(userId: String): Flow<Resource<List<ModelChat>>>

    fun getAllMessagesOfChat(chatId: String): Flow<Resource<List<ModelMessage>>>

    fun sendMessage(chatId: String, messageModel: ModelMessage): Flow<Resource<Boolean>>
}