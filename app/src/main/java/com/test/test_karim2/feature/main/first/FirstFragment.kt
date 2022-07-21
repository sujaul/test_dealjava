package com.test.test_karim2.feature.main.first

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chareem.core.BaseFragment
import com.chareem.core.Constant
import com.chareem.core.data.BaseResponse
import com.test.test_karim2.R
import com.test.test_karim2.databinding.FragmentFirstBinding
import com.test.test_karim2.feature.ImageViewActivity
import com.test.test_karim2.feature.ItemFilm
import com.test.test_karim2.feature.main.MainActivity
import com.test.test_karim2.util.gone
import com.test.test_karim2.util.visible
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.androidx.viewmodel.ext.android.viewModel


class FirstFragment : BaseFragment<FragmentFirstBinding>() {

    companion object {
        const val TAG = "FirstFragment"
    }

    private val filmGroupAdapter = GroupAdapter<GroupieViewHolder>()
    private val navController by lazy { findNavController() }
    private val vmFirst: FirstVM by viewModel()
    private val data_pref by lazy {
        requireContext().getSharedPreferences(Constant.data_pref, Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()
        val activity = mActivity as MainActivity
        activity.setTittel("Home", isVisibleBack = false )
    }

    override fun getTagName(): String = TAG
    override fun onCreateUI(view: View, savedInstanceState: Bundle?) {
        init()
    }

    private fun init(){
        setupRecycler()
        observVm()
        binding.searchBt.setOnClickListener {
            val searchtxt = binding.searchEt.text.toString().trim { it <= ' '}
            vmFirst.serchFilm(searchtxt, mContext)
        }
        binding.refreshSrl.setOnRefreshListener {
            val searchtxt = binding.searchEt.text.toString().trim { it <= ' '}
            vmFirst.serchFilm(searchtxt, mContext)
        }
        val text = "<font color=#000000>Don't have account ? </font><font color=#d84372>Register</font>"
        val htmTxt = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        vmFirst.serchFilm("", mContext)
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
                    binding.refreshSrl.isRefreshing = true
                    //showDialogLoading("Loading, please wait...")
                }
                is BaseResponse.Success -> {
                    binding.refreshSrl.isRefreshing = false
                    //hideDialogLoading()

                    if (response.data.isEmpty()){
                        binding.emptyContent.rlEmpty.visible()
                        binding.recFirstLl.gone()
                        showSnackBarMessage("danger", "Authentication failed, please check your username or passford")
                    } else {
                        binding.emptyContent.rlEmpty.gone()
                        binding.recFirstLl.visible()
                        filmGroupAdapter.clear()
                        response.data.map {
                            filmGroupAdapter.add(ItemFilm(it, { film, stok ->
                                vmFirst.addStok(film, stok)
                            }, { film, stok ->
                                vmFirst.minusStok(film, stok)
                            }, { film, pos ->
                                startActivity(ImageViewActivity.newInstance(mContext, pos.toString(), film.films.name, film.films.url))
                            }))
                        }
                    }
                }
                is BaseResponse.Error->{
                    binding.refreshSrl.isRefreshing = false
                    //hideDialogLoading()
                    showSnackBarMessage("danger", response.message)
                }
            }
        })
    }

    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Boolean): FragmentFirstBinding {
        return FragmentFirstBinding.inflate(inflater, container, savedInstanceState)
    }

    private fun setupRecycler(){
        val recycler = binding.recyclerFirst
        recycler.apply {
            val linearLayout = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            layoutManager = linearLayout
            adapter = filmGroupAdapter
        }
    }
}
