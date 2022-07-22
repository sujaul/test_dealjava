package com.test.test_karim2.data.model

import com.google.gson.JsonArray

data class BookVolumeInfo(
        var title: String = "",
        var authors: JsonArray? = null,
        var categories: JsonArray? = null,
        var publisher: String? = null,
        var publishedDate: String? = null,
        var imageLinks: BookImageLink? = null
)
