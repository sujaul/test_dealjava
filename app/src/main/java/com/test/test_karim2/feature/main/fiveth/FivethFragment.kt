package com.test.test_karim2.feature.main.fiveth

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.chareem.core.BaseFragment
import com.chareem.core.Constant
import com.google.android.material.navigation.NavigationView
import com.test.test_karim2.R
import com.test.test_karim2.databinding.FragmentFivethBinding
import com.test.test_karim2.databinding.NavHeaderMainBinding
import com.test.test_karim2.feature.main.MainActivity
import com.test.test_karim2.feature.main.third.ThirdFragment


class FivethFragment : BaseFragment<FragmentFivethBinding>() {

    companion object {
        val REQUEST_KEY = "FivethFragment_REQUEST_KEY"
        const val TAG = "FivethFragment"
        val username = "username"
    }
    private val navController by lazy { findNavController() }
    private val data_pref by lazy {
        requireContext().getSharedPreferences(Constant.data_pref, Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()
        val activity = requireActivity() as MainActivity
        activity.setTittel("Fiveth Screen", false)
    }

    override fun getTagName(): String = TAG
    override fun onCreateUI(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        init()
    }

    private fun init(){
        binding.txtName.text = requireArguments().getString(username)
        val headerView: View = binding.navView.getHeaderView(0)
        requireArguments().getString(ThirdFragment.username)?.let {
            val navHeaderBinding = NavHeaderMainBinding.bind(headerView)
            navHeaderBinding.tvHeaderDrawer.text = "Welcome, ${it}"
        }
        binding.toolbarFiveth.imgHamburger.setOnClickListener {
            binding.drawerLayout.openDrawer(Gravity.LEFT);
        }
        setupDrawerContent(binding.navView)
        binding.btnLogout.setOnClickListener {
            val data_edt = data_pref.edit()
            data_edt.putString(MainActivity.username, null)
            data_edt.apply()
            setFragmentResult(REQUEST_KEY, bundle)
            navController.navigateUp()
        }
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.getMenu().getItem(1).setChecked(true)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            selectDrawerItem(menuItem)
            true
        }
    }
    private fun selectDrawerItem(menuItem: MenuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        when (menuItem.getItemId()) {
            R.id.nav_home -> {
                bundle.putString(ThirdFragment.username, requireArguments().getString(ThirdFragment.username))
                navController.popBackStack(R.id.nav_fiveth, true)
                navController.navigate(R.id.nav_third, bundle)
            }
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.isChecked = true
        // Close the navigation drawer
        binding.drawerLayout.closeDrawers()
    }

    override fun setBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Boolean
    ): FragmentFivethBinding {
        return FragmentFivethBinding.inflate(inflater, container, savedInstanceState)
    }
}
