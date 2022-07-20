package com.test.test_karim2.feature.main.fourth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.galee.core.BaseFragment
import com.test.test_karim2.R
import com.test.test_karim2.databinding.FragmentFourthBinding
import com.test.test_karim2.feature.main.MainActivity


class FourthFragment : BaseFragment<FragmentFourthBinding>() {

    companion object {
        val username = "username"
        val avatar = "abatar"
        const val TAG = "UserDetailFragment"
    }
    private val navController by lazy { findNavController() }

    override fun onResume() {
        super.onResume()
        val activity = requireActivity() as MainActivity
        activity.setTittel("Detail")
    }

    override fun getTagName(): String = TAG
    override fun onCreateUI(view: View, savedInstanceState: Bundle?) {
        init()
    }

    private fun init(){
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_baseline_groups_24)
            .error(R.drawable.ic_baseline_groups_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
        Glide.with(mContext)
            .load(requireArguments().getString(avatar))
            .apply(options)
            .into(binding.imgAvatar)
        binding.txtName.text = requireArguments().getString(username)
    }

    override fun setBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Boolean
    ): FragmentFourthBinding {
        return FragmentFourthBinding.inflate(inflater, container, savedInstanceState)
    }
}
