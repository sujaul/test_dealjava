package com.test.test_karim2.feature.main

import android.os.Bundle
import androidx.navigation.findNavController
import com.chareem.core.BaseActivity
import com.test.test_karim2.R
import com.test.test_karim2.databinding.ActivityMainBinding
import com.test.test_karim2.util.gone
import com.test.test_karim2.util.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.component.KoinComponent
import kotlin.coroutines.CoroutineContext

class MainActivity : BaseActivity<ActivityMainBinding>(), KoinComponent {

    companion object{
        const val TAG = "MainActivity"
    }


    private val navController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    override fun getTagName(): String = TAG

    override fun onCreateUI(savedInstanceState: Bundle?) {
        init(savedInstanceState)
    }

    override fun onAllPermissionGranted(level: Int) {
        super.onAllPermissionGranted(level)
    }

    override fun onDenyPermission(level: Int) {
        super.onDenyPermission(level)
        rationaleCallback()
    }

    private fun init(savedInstanceState: Bundle?){
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.mobile_navigation)
        if (savedInstanceState == null) {
            graph.setStartDestination(R.id.nav_first)
        }
        navController.setGraph(graph);
    }

    override fun onSupportNavigateUp(): Boolean {
        return /*navController.navigateUp(appBarConfiguration) ||*/ super.onSupportNavigateUp()
    }

    fun setTittel(tittle: String, isVisible: Boolean = true, isVisibleBack : Boolean = true){
        binding.titleBar.text = tittle
        if (isVisible) {
            setSupportActionBar(binding.toolbar)
            binding.imgBack.setOnClickListener {
                super.onBackPressed()
            }
            binding.appbar.visible()
            if (isVisibleBack)
                binding.imgBack.visible()
            else binding.imgBack.gone()
        }
        else {
            setSupportActionBar(binding.toolbar)
            binding.imgBack.setOnClickListener(null)
            binding.appbar.gone()
        }
    }

    override fun setBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}