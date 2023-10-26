package com.hfad.whatsappclone.domain.use_case

import com.hfad.whatsappclone.domain.model.ModelUser
import com.hfad.whatsappclone.domain.repository.AuthRepository
import com.hfad.whatsappclone.presentation.MainActivity
import javax.inject.Inject

class AuthenticationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    fun phoneNumberSignIn(phoneNumber: String, activity: MainActivity) =
        authRepository.phoneNumberSignIn(activity = activity, phoneNumber = phoneNumber)

    fun isUserAuthenticated() = authRepository.isUserAuthenticated()

    fun getUserId() = authRepository.getUserId()

    fun createUserProfile(modelUser: ModelUser, userId: String) = authRepository.createUserProfile(modelUser)
}