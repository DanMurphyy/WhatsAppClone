package com.hfad.whatsappclone.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.whatsappclone.domain.model.ModelUser
import com.hfad.whatsappclone.domain.use_case.AuthenticationUseCase
import com.hfad.whatsappclone.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase
) : ViewModel() {
    lateinit var iViews: IViews

    fun signInWithPhoneNumber(phoneNumber: String, activity: MainActivity) {
        iViews = activity
        viewModelScope.launch {
            authUseCase.phoneNumberSignIn(phoneNumber, activity).collect {
                when (it) {
                    is Resource.Loading -> {
                        iViews.showProgressBar()
                    }
                    is Resource.Error -> {
                        iViews.showError(it.message ?: "An Error")
                    }
                    is Resource.Success -> {
                        iViews.hideProgressBar()
                        iViews.dismissOtpFragmentBottomSheetDialog()
                        iViews.changeViewVisibility()
                    }
                }
            }
        }
    }

    fun createUserProfile(modelUser: ModelUser) {
        viewModelScope.launch {
            authUseCase.createUserProfile(modelUser, authUseCase.getUserId()).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        iViews.showProgressBar()
                    }
                    is Resource.Error -> {
                        iViews.showError(it.message ?: "An Error")
                    }
                    is Resource.Success -> {
                        iViews.hideProgressBar()
                        iViews.openHomePageLayout()
                    }
                }
            }
        }
    }
    fun isUserAuthenticated() = authUseCase.isUserAuthenticated()

    fun getUserId() = authUseCase.getUserId()

}