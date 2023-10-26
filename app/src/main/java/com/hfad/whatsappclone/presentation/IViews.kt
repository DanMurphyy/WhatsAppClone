package com.hfad.whatsappclone.presentation

interface IViews {
    fun showProgressBar() {}

    fun hideProgressBar() {}

    fun showError(error: String) {}

    fun dismissOtpFragmentBottomSheetDialog() {}

    fun changeViewVisibility() {}

    fun openHomePageLayout() {}

}