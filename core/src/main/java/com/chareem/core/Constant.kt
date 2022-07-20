package com.chareem.core

import android.Manifest

object Constant {
    val data_pref = "data.pref"
    const val DATABASE_NAME = "data.db" // nama database
    var APP_ID = "App" // ambil dari manifest app

    var BASE_URL = BuildConfig.URL_DEBUG
    val page = 20
    val permissions = mutableListOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
}