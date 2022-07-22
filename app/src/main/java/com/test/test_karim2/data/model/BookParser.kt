package com.test.test_karim2.data.model

data class BookParser(
        var id: String = "",
        var volumeInfo: BookVolumeInfo? = null,
        var searchInfo: BookSearchInfo? = null
)
