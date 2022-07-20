package com.test.test_karim2.feature.main.third

import android.os.Bundle
import android.view.*
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chareem.core.BaseFragment
import com.chareem.core.data.BaseResponse
import com.chareem.core.util.NetworkUtil
import com.google.android.material.navigation.NavigationView
import com.test.test_karim2.R
import com.test.test_karim2.data.model.Persons
import com.test.test_karim2.databinding.FragmentThirdBinding
import com.test.test_karim2.databinding.NavHeaderMainBinding
import com.test.test_karim2.feature.ItemPerson
import com.test.test_karim2.feature.main.MainActivity
import com.test.test_karim2.feature.main.fiveth.FivethFragment
import com.test.test_karim2.feature.main.fourth.FourthFragment
import com.test.test_karim2.util.gone
import com.test.test_karim2.util.visible
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.androidx.viewmodel.ext.android.viewModel


class ThirdFragment : BaseFragment<FragmentThirdBinding>() {

    companion object {
        val username = "username"
        const val TAG = "ThirdFragment"
    }

    private val personsGroupAdapter = GroupAdapter<GroupieViewHolder>()
    private val navController by lazy { findNavController() }
    private var listPersons: List<Persons> = arrayListOf()
    private val vmThird: ThirdVM by viewModel()

    override fun onResume() {
        super.onResume()
        val activity = requireActivity() as MainActivity
        activity.setTittel("Third Screen", false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getTagName(): String = TAG
    override fun onCreateUI(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        init()
    }

    private fun init(){
        setupRecycler()
        observVM()
        val headerView: View = binding.navView.getHeaderView(0)
        requireArguments().getString(username)?.let {
            val navHeaderBinding = NavHeaderMainBinding.bind(headerView)
            navHeaderBinding.tvHeaderDrawer.text = "Welcome, ${it}"
        }
        showEmptyView(true)
        getPerson()
        binding.toolbarThird.imgHamburger.setOnClickListener {
            binding.drawerLayout.openDrawer(Gravity.LEFT);
        }
        setupDrawerContent(binding.navView)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.getMenu().getItem(0).setChecked(true)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            selectDrawerItem(menuItem)
            true
        }
    }
    private fun selectDrawerItem(menuItem: MenuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        when (menuItem.getItemId()) {
            R.id.nav_home -> getPerson()
            R.id.nav_profile -> {
                setFragmentResultListener(FivethFragment.REQUEST_KEY) { key, bundle ->
                    navController.navigateUp()
                }
                bundle.putString(FivethFragment.username, requireArguments().getString(username))
                navController.popBackStack(R.id.nav_third, true)
                navController.navigate(R.id.nav_fiveth,bundle)
            }
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.isChecked = true
        // Close the navigation drawer
        binding.drawerLayout.closeDrawers()
    }

    private fun setupRecycler(){
        val recycler = binding.recyclerThird
        recycler.apply {
            val linearLayout = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            layoutManager = linearLayout
            adapter = personsGroupAdapter
        }
    }

    private fun showEmptyView(status: Boolean){
        if (isRemoving) return
        if (status) {
            binding.recThirdLl.gone()
            binding.emptyContent.rlEmpty.visible()
        } else {
            binding.recThirdLl.visible()
            binding.emptyContent.rlEmpty.gone()
        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.menu_events, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }*/

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ac_map -> {
                openFivethScreen()
            }
            R.id.ac_search -> {

            }
        }
        return false
    }*/
    private fun getPerson(){
        if (NetworkUtil.isNetworkConnected(mContext)){
            vmThird.getPersons(1, 12)
        } else showSnackBarMessage("danger", getString(R.string.error_connection))
    }

    private fun observVM() {

        vmThird.getUsers.observe(this, Observer { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    showDialogLoading("Get data, please wait...")
                }
                is BaseResponse.Success -> {
                    hideDialogLoading()
                    if (personsGroupAdapter.itemCount > 0)
                        personsGroupAdapter.clear()
                    listPersons = response.data
                    response.data.map {
                        personsGroupAdapter.add(ItemPerson(it){ person, pos ->
                            bundle.putString(FourthFragment.avatar, person.avatar)
                            bundle.putString(FourthFragment.username, person.first_name +" "+person.last_name)
                            navController.navigate(R.id.nav_fourth,bundle)
                        })
                    }
                    if (response.data.isEmpty()) {
                        showSnackBarMessage("success", "No data")
                        showEmptyView(true)
                    } else {
                        showEmptyView(false)
                    }
                }
                is BaseResponse.Error -> {
                    hideDialogLoading()
                    showSnackBarMessage("danger", response.message)
                }
            }
        })
    }

    private fun openFivethScreen(){
        /*val data = Gson().toJson(listEvents)
        bundle.putString(FivethFragment.events_data, data)
        navController.navigate(R.id.nav_fiveth,bundle)*/
    }

    override fun setBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Boolean
    ): FragmentThirdBinding {
        return FragmentThirdBinding.inflate(inflater, container, savedInstanceState)
    }
}
