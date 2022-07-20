package com.test.test_karim2.feature.main.second

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chareem.core.BaseFragment
import com.chareem.core.data.BaseResponse
import com.test.test_karim2.data.model.Register
import com.test.test_karim2.databinding.FragmentSecondBinding
import com.test.test_karim2.feature.main.AuthVM
import com.test.test_karim2.feature.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class SecondFragment : BaseFragment<FragmentSecondBinding>() {

    companion object {
        const val TAG = "SecondFragment"
    }

    private val navController by lazy { findNavController() }
    private val vmAuth: AuthVM by viewModel()

    override fun onResume() {
        super.onResume()
        val activity = mActivity as MainActivity
        activity.setTittel("Second Screen", false)
    }

    override fun getTagName(): String = TAG
    override fun onCreateUI(view: View, savedInstanceState: Bundle?) {
        init()
    }

    private fun init(){
        observVm()
        binding.btnRegister.setOnClickListener {
            validaetRegister()
        }
    }

    private fun validaetRegister(){
        if (binding.edtFullname.text.isNullOrEmpty()){
            showSnackBarMessage("info", "Full name should not be empty")
            return
        }
        if (binding.edtEmail.text.isNullOrEmpty()){
            showSnackBarMessage("info", "Email should not be empty")
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.text.toString()).matches()){
            showSnackBarMessage("info", "Email not valid")
            return
        }
        if (binding.edtPassword.text.isNullOrEmpty()){
            showSnackBarMessage("info", "Password should not be empty")
            return
        }
        val genderPos = binding.spGender.selectedItemPosition
        if (genderPos == 0){
            showSnackBarMessage("info", "Please choose gender first")
            return
        }
        val reg = Register()
        reg.fullname = binding.edtFullname.text.toString()
        reg.email = binding.edtEmail.text.toString()
        reg.password = binding.edtPassword.text.toString()
        reg.phone = binding.edtPhone.text.toString()
        reg.gender = if (genderPos == 1) "M" else "F"
        reg.fullname = binding.edtFullname.text.toString()
        vmAuth.registerUser(reg)
    }

    private fun observVm(){
        vmAuth.registerUsers.observe(this, Observer { response ->
            when(response){
                is BaseResponse.Loading-> {
                    showDialogLoading("Logging in, please wait...")
                }
                is BaseResponse.Success -> {
                    hideDialogLoading()

                    if (response.data.status){
                        showToastMessage("success", response.data.message)
                        navController.navigateUp()
                    } else {
                        showToastMessage("danger", response.data.message)
                    }
                }
                is BaseResponse.Error->{
                    hideDialogLoading()
                    showSnackBarMessage("danger", response.message)
                }
            }
        })
    }

    override fun setBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Boolean
    ): FragmentSecondBinding {
        return FragmentSecondBinding.inflate(inflater, container, savedInstanceState)
    }
}
