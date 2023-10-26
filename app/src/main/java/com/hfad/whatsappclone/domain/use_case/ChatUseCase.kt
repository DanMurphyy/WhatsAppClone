package com.hfad.whatsappclone.domain.use_case

import com.hfad.whatsappclone.domain.model.ModelMessage
import com.hfad.whatsappclone.domain.repository.UserRepository
import javax.inject.Inject

class ChatUseCase @Inject constructor(
    private var userRepository: UserRepository
) {
    fun getAllChats(userId: String) = userRepository.getAllChats(userId)

    fun getAllMessagesOfChat(chatId: String) = userRepository.getAllMessagesOfChat(chatId)

    fun sendMessage(chatId: String, messageModel: ModelMessage) =
        userRepository.sendMessage(chatId, messageModel)
}