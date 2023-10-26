package com.hfad.whatsappclone.presentation.homePageLayout.message

import com.hfad.whatsappclone.presentation.IViews

interface IMessageView : IViews {
    fun getUserId(): String
    fun clearMessages() {}
}