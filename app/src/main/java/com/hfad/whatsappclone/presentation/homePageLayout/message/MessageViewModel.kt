package com.hfad.whatsappclone.presentation.homePageLayout.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.whatsappclone.domain.model.ModelMessage
import com.hfad.whatsappclone.domain.use_case.AuthenticationUseCase
import com.hfad.whatsappclone.domain.use_case.ChatUseCase
import com.hfad.whatsappclone.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val chatUseCase: ChatUseCase,
    private val authUseCase: AuthenticationUseCase
) : ViewModel() {

    private var _messages = MutableStateFlow<List<ModelMessage>>(emptyList())
    val messages: StateFlow<List<ModelMessage>> = _messages

    lateinit var iMessageView: IMessageView

    fun getAllChatsMessages(chatId: String, listener: IMessageView) = viewModelScope.launch {
        iMessageView = listener
        chatUseCase.getAllMessagesOfChat(chatId).collectLatest {
            when (it) {
                is Resource.Success -> {
                    iMessageView.hideProgressBar()
                    _messages.value = it.data
                }
                is Resource.Loading -> {
                    iMessageView.showProgressBar()
                }
                is Resource.Error -> {
                    iMessageView.showError(it.message ?: "An Error Occurred")
                }
            }
        }
    }

    fun getUserId() = authUseCase.getUserId()

    fun sendMessage(chatId: String, messageModel: ModelMessage) = viewModelScope.launch {
        chatUseCase.sendMessage(chatId, messageModel).collectLatest {
            when (it) {
                is Resource.Loading -> {
                    iMessageView.showProgressBar()
                }
                is Resource.Error -> {
                    iMessageView.showError(it.message ?: "An Error")
                }
                is Resource.Success -> {
                    iMessageView.hideProgressBar()
                    iMessageView.clearMessages()
                }
            }
        }
    }
}