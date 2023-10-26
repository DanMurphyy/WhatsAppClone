package com.hfad.whatsappclone.presentation.homePageLayout.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.whatsappclone.domain.model.ModelUser
import com.hfad.whatsappclone.domain.use_case.ContactsUseCase
import com.hfad.whatsappclone.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private var contactsUseCase: ContactsUseCase
) : ViewModel() {
    private val _whatsAppContactsList = MutableStateFlow<List<ModelUser>>(emptyList())
    val whatsAppContactsList: StateFlow<List<ModelUser>> = _whatsAppContactsList

    private lateinit var iContactsView: IContactsView

    fun getAllWhatsAppContacts(deviceContacts: List<String>, interfaceListener: IContactsView) =
        viewModelScope.launch {
            iContactsView = interfaceListener
            contactsUseCase.getAllUsers(deviceContacts).collectLatest {
                when (it) {
                    is Resource.Success -> {
                        iContactsView.hideProgressBar()
                        _whatsAppContactsList.value = it.data
                    }
                    is Resource.Loading -> {
                        iContactsView.showProgressBar()
                    }
                    is Resource.Error -> {
                        iContactsView.showError(it.message ?: "An Error Occurred")
                        iContactsView.hideProgressBar()
                    }
                }
            }
        }
}