package com.test.test_karim2.data.remote

import com.test.test_karim2.data.model.Persons

data class GuestResponse(
    var page: Int? = null,
    var per_page: Int? = null,
    var total: Int? = null,
    var total_pages: Int? = null,
    var data: List<Persons>? = null
)