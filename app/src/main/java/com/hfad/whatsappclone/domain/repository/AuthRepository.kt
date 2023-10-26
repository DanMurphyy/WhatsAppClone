package com.hfad.whatsappclone.domain.repository

import com.google.firebase.auth.PhoneAuthCredential
import com.hfad.whatsappclone.domain.model.ModelUser
import com.hfad.whatsappclone.presentation.MainActivity
import com.hfad.whatsappclone.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun phoneNumberSignIn(phoneNumber: String, activity: MainActivity): Flow<Resource<Boolean>>

    fun isUserAuthenticated(): Boolean

    fun getUserId(): String

    suspend fun signInWithAuthCredential(phoneAuthCredential: PhoneAuthCredential): Resource<Boolean>

    fun createUserProfile(modelUser: ModelUser): Flow<Resource<Boolean>>
}