package com.test.test_karim2.data.model

data class BookResponse(
        var items: List<BookParser>? = null,
        var totalItems: Int = 0,
        var kind: String = "",
)
