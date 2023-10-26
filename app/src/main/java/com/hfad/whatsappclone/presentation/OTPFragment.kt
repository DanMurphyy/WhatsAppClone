package com.hfad.whatsappclone.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hfad.whatsappclone.databinding.FragmentOTPBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OTPFragment : BottomSheetDialogFragment(), IViews {

    private var _binding: FragmentOTPBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOTPBinding.inflate(inflater, container, false)
        setUpOTPInput()
        return binding.root
    }

    private fun setUpOTPInput() {
        binding.let { it ->
            it.otpBox1.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let { first ->
                        if (first.length == 1) {
                            it.otpBox2.requestFocus()
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            it.otpBox2.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let { first ->
                        if (first.length == 1) {
                            it.otpBox3.requestFocus()
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            it.otpBox3.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let { first ->
                        if (first.length == 1) {
                            it.otpBox4.requestFocus()
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            it.otpBox4.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let { first ->
                        if (first.length == 1) {
                            it.otpBox5.requestFocus()
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            it.otpBox5.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let { first ->
                        if (first.length == 1) {
                            it.otpBox6.requestFocus()
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            it.otpBox6.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let { first ->
                        if (first.length == 1) {
                            val otpValue = it.otpBox1.text.toString() + it.otpBox2.text.toString() +
                                    it.otpBox3.text.toString() + it.otpBox4.text.toString() +
                                    it.otpBox5.text.toString() + it.otpBox6.text.toString()
                            (activity as MainActivity).otpValue.value = otpValue
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }
    }
    override fun dismissOtpFragmentBottomSheetDialog() {
        dismiss()
    }

}