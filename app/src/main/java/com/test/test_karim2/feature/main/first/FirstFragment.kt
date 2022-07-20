package com.test.test_karim2.feature.main.first

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chareem.core.BaseFragment
import com.chareem.core.Constant
import com.chareem.core.data.BaseResponse
import com.test.test_karim2.R
import com.test.test_karim2.databinding.FragmentFirstBinding
import com.test.test_karim2.feature.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class FirstFragment : BaseFragment<FragmentFirstBinding>() {

    companion object {
        const val TAG = "FirstFragment"
    }

    private val navController by lazy { findNavController() }
    private val vmFirst: FirstVM by viewModel()
    private var isFirstLoad = false
    private val data_pref by lazy {
        requireContext().getSharedPreferences(Constant.data_pref, Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()
        val activity = mActivity as MainActivity
        activity.setTittel("Home", true)
    }

    override fun getTagName(): String = TAG
    override fun onCreateUI(view: View, savedInstanceState: Bundle?) {
        init()
    }

    private fun init(){
        observVm()
        binding.searchBt.setOnClickListener {
            val searchEt = binding.searchEt.text
            validaetSearch(searchEt.toString())
        }
        val text = "<font color=#000000>Don't have account ? </font><font color=#d84372>Register</font>"
        val htmTxt = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        vmFirst.serchFilm("action", mContext)
    }

    private fun validaetSearch(genre: String?){
        if (genre.isNullOrEmpty()){
            showSnackBarMessage("info", "Username should not be empty")
            return
        }
        vmFirst.serchFilm(genre, mContext)
    }

    private fun openSecondScreen(){
        navController.navigate(R.id.nav_second,bundle)
    }

    private fun openThirdScreen(){
        /*val data_edt = data_pref.edit()
        data_edt.putString(MainActivity.username, binding.edtEmail.text.toString())
        data_edt.apply()
        bundle.putString(ThirdFragment.username, binding.edtEmail.text.toString())*/
        navController.popBackStack(R.id.nav_first, true)
        navController.navigate(R.id.nav_third,bundle)
    }

    private fun observVm(){
        vmFirst.searchFilm.observe(this, Observer { response ->
            when(response){
                is BaseResponse.Loading-> {
                    isFirstLoad = true
                    showDialogLoading("Logging in, please wait...")
                }
                is BaseResponse.Success -> {
                    hideDialogLoading()
                    if (isFirstLoad){
                        if (response.data.isEmpty()){
                            showSnackBarMessage("danger", "Authentication failed, please check your username or passford")
                        } else {
                            openThirdScreen()
                        }
                    }
                    isFirstLoad = false
                }
                is BaseResponse.Error->{
                    isFirstLoad = false
                    hideDialogLoading()
                    showSnackBarMessage("danger", response.message)
                }
            }
        })
    }

    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Boolean): FragmentFirstBinding {
        return FragmentFirstBinding.inflate(inflater, container, savedInstanceState)
    }
}
