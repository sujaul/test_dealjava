package com.chareem.core

import android.Manifest
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.chareem.core.helper.widget.SnackBarCustomImpl
import com.chareem.core.helper.widget.ToastCustomImpl
import com.chareem.core.util.hideKeyboard
import com.chareem.core.util.hideSoftKeyboard
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity<VB: ViewBinding> : AppCompatActivity(), BaseImplDialog,
    BaseImplSnackBar, BaseImplToast, BaseImplPermission, CoroutineScope {

    abstract fun getTagName(): String
    abstract fun onCreateUI(savedInstanceState: Bundle?)
    lateinit var binding: VB
    abstract fun setBinding(): VB

    private var pDialog: Dialog? = null
    private var snackbar : Snackbar? = null
    protected val bundle = Bundle()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun onStart() {
        super.onStart()
        Timber.i("${getTagName()} onStart() called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setBinding()
        Timber.i(" ${getTagName()} onCreate() called")
        hideKeyboard()
        hideSoftKeyboard()
        setContentView(binding.root)
        if (savedInstanceState == null) onCreateUI(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        Timber.i(" ${getTagName()} onResume() called")
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        //onRunPermissionX(Constant.permissions, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.i(" ${getTagName()} onSaveInstanceState() called")
    }

    override fun onDestroy() {
        hideDialogLoading()
        super.onDestroy()
        Timber.i("${getTagName()} onDestroy() called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("${getTagName()} onPause() called")
    }

    override fun showDialogLoading(message: String?) {
        hideDialogLoading()
        if (pDialog == null) pDialog = Dialog(this)

        pDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        pDialog?.setContentView(R.layout.dialog_loading)
        pDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        val progressBar = pDialog?.findViewById(R.id.progressBar) as ProgressBar
        val msg = pDialog?.findViewById(R.id.txt_message) as TextView
        if (message != null) msg.text = message
        else msg.text = getString(R.string.base_txt_loading)
        pDialog?.setCancelable(false)
        pDialog?.show()
    }

    override fun hideDialogLoading() {
        if (pDialog != null && pDialog?.isShowing!!) {
            pDialog?.dismiss(); pDialog = null
        }
    }

    override fun showToastMessage(message: String, duration: Int) {
        ToastCustomImpl("").getToast(this, message, duration).show()
    }

    /**
     * @param type => 'success', 'danger', 'info' or '' as String
     * @param message => message of Toast
     * @param duration => duration of Toast
     **/
    override fun showToastMessage(type: String, message: String, duration: Int) {
        ToastCustomImpl(type).getToast(this, message, duration).show()
    }

    override fun showSnackBarMessage(
        message: String,
        duration: Int,
        label: String,
        listener: (() -> Unit)?
    ): Snackbar? {
        dismissSnackBar()
        snackbar = SnackBarCustomImpl("").getSnackbar(this, message, duration, label, listener)
        snackbar?.show()
        return snackbar
    }

    /**
     * @param type => 'success', 'danger', 'info' or '' as String
     * @param message => message of Snackbar
     * @param duration => duration of Snackbar
     **/
    override fun showSnackBarMessage(
        type: String,
        message: String,
        duration: Int,
        label: String,
        listener: (() -> Unit)?
    ): Snackbar? {
        dismissSnackBar()
        snackbar = SnackBarCustomImpl(type).getSnackbar(this, message, duration, label, listener)
        snackbar?.show()
        return snackbar
    }

    override fun dismissSnackBar() {
        snackbar?.let {
            if (it.isShown) it.dismiss()
        }
    }

    protected fun isHasPermission(permissions: MutableList<String>): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            permissions.all { singlePermission ->
                applicationContext.checkSelfPermission(singlePermission) == PackageManager.PERMISSION_GRANTED
            }
        else true
    }

    protected fun onRunPermission(permissions: MutableList<String>, level: Int) {
        Timber.i("${getTagName()} onRunPermission() called")
        Dexter.withActivity(this).withPermissions(permissions).withListener(
                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (it.areAllPermissionsGranted()) {
                                onAllPermissionGranted(level)
                            } else {
                                onDenyPermission(level)
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                }
            ).withErrorListener {
                Toast.makeText(this@BaseActivity, "Error occurred! ", Toast.LENGTH_SHORT).show()
            }
            .onSameThread()
            .check()
    }

    protected fun onRunPermissionX(permissions: MutableList<String>, level: Int) {
        Timber.i("${getTagName()} onRunPermission() called")
        PermissionX.init(this)
            .permissions(permissions)
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "You need to grant all permission in Settings manually",
                    "OK",
                    "Cancel"
                )
            }.request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!Settings.canDrawOverlays(applicationContext)) {
                            val intent = Intent(
                                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + application.packageName)
                            )
                            startActivity(intent)
                        }
                    }*/
                    onAllPermissionGranted(level)
                } else {
                    onDenyPermission(level)
                }
            }
    }

    override fun onAllPermissionGranted(level: Int) {
        Timber.i("${getTagName()} onAllPermissionGranted() called")
    }

    override fun onDenyPermission(level: Int) {
        finish()
        Timber.i("${getTagName()} onDenyPermission() called")
    }

    protected fun rationaleCallback() {
        AlertDialog.Builder(this)
            .setTitle("Permissions Denied")
            .setMessage("This is the custom rationale dialog. Please allow us to proceed asking for permissions again, or cancel to end the permission flow and applicatons.")
            .setPositiveButton("Go Ahead") { _, which -> onRunPermission(Constant.permissions, 2) }
            .setNegativeButton("cancel") { _, which -> finish() }
            .setCancelable(false)
            .show()
    }
}

interface BaseImplPermission {
    fun onAllPermissionGranted(level: Int)
    fun onDenyPermission(level: Int)
}

interface BaseImplDialog {
    fun showDialogLoading(message: String?)
    fun hideDialogLoading()
}

interface BaseImplToast {
    fun showToastMessage(message: String, duration: Int = Toast.LENGTH_LONG)
    fun showToastMessage(type: String, message: String, duration: Int = Toast.LENGTH_LONG)
}

interface BaseImplSnackBar {
    fun showSnackBarMessage(message: String, duration: Int = Snackbar.LENGTH_LONG,
                            label:String = "Ok", listener: (() -> Unit)?=null): Snackbar?
    fun showSnackBarMessage(type: String, message: String, duration: Int = Snackbar.LENGTH_LONG,
                            label:String = "Ok", listener: (() -> Unit)?=null): Snackbar?
    fun dismissSnackBar()
}