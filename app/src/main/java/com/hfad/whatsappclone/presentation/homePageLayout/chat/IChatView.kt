package com.hfad.whatsappclone.presentation.homePageLayout.chat

import com.hfad.whatsappclone.presentation.IViews

interface IChatView : IViews{
    fun openMessageFragment(chatId: String) {}
}