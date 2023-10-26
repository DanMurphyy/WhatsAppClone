package com.hfad.whatsappclone.domain.use_case

import com.hfad.whatsappclone.domain.repository.UserRepository
import javax.inject.Inject

class ContactsUseCase @Inject constructor(
    private var userRepository: UserRepository
) {
    fun getAllUsers(deviceContacts: List<String>) = userRepository.getAllUsers(deviceContacts)
}