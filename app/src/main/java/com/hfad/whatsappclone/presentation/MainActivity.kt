package com.hfad.whatsappclone.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hfad.whatsappclone.R
import com.hfad.whatsappclone.databinding.ActivityMainBinding
import com.hfad.whatsappclone.domain.model.ModelUser
import com.hfad.whatsappclone.presentation.homePageLayout.HomePageFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), IViews {
    private lateinit var binding: ActivityMainBinding
    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    val otpValue = MutableStateFlow<String>("")
    lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (checkAuthenticationStatus()) {
            //new
            openHomePage()
        } else {
            binding.userAuthenticationLayout.visibility = View.VISIBLE
            binding.appLogo.visibility = View.VISIBLE
            binding.btProceed.visibility = View.VISIBLE
            binding.userNumberLayout.visibility = View.VISIBLE
            binding.textInputLayout1.visibility = View.VISIBLE
        }

        binding.btProceed.setOnClickListener {
            if (binding.etNumber.isVisible) {
                phoneNumber = "+998 ${binding.etNumber.text.toString()}"
                authenticationViewModel.signInWithPhoneNumber(phoneNumber, this)
            } else {
                val modelUser: ModelUser =
                    ModelUser(userName = binding.etName.text.toString(), userNumber = phoneNumber)
                authenticationViewModel.createUserProfile(modelUser)
            }

        }
    }

    private fun checkAuthenticationStatus(): Boolean {
        return authenticationViewModel.isUserAuthenticated()
    }

    fun showBottomSheet() {
        Log.d("MainActivity", "showBottomSheet: called")

        val otpFragment = OTPFragment()
        supportFragmentManager.beginTransaction().add(otpFragment, "bottomSheetOtpFragment")
            .commit()
    }

    override fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.userAuthenticationLayout.visibility = View.GONE
    }

    override fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.userAuthenticationLayout.visibility = View.VISIBLE

    }

    override fun showError(error: String) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun dismissOtpFragmentBottomSheetDialog() {
        val fragment = supportFragmentManager.findFragmentByTag("bottomSheetOtpFragment")
        fragment?.let {
            (it as BottomSheetDialogFragment).dismiss()
        }
    }

    override fun changeViewVisibility() {
        binding.userNumberLayout.visibility = View.GONE
        binding.textInputLayout1.visibility = View.GONE
        binding.etNumber.visibility = View.GONE
        binding.userNameLayout.visibility = View.VISIBLE
        binding.textInputLayout2.visibility = View.VISIBLE
        binding.etName.visibility = View.VISIBLE
    }

    override fun openHomePageLayout() {
        //new
        openHomePage()
        Toast.makeText(this, "HomePage Layout", Toast.LENGTH_SHORT).show()
    }

    //new
    private fun openHomePage() {
        setAllMainActivityViewsGone()
        binding.fragmentContainer.visibility = View.VISIBLE
        val homePageFragment = HomePageFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, homePageFragment, "homePageFragment").commit()
    }

    //new
    private fun setAllMainActivityViewsGone() {
        binding.userAuthenticationLayout.visibility = View.GONE
        binding.appLogo.visibility = View.GONE
        binding.userNameLayout.visibility = View.GONE
        binding.textInputLayout2.visibility = View.GONE
        binding.etName.visibility = View.GONE
        binding.btProceed.visibility = View.GONE
        binding.userNumberLayout.visibility = View.GONE
        binding.textInputLayout1.visibility = View.GONE
        binding.etNumber.visibility = View.GONE
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}