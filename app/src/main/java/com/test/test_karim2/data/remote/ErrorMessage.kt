package com.test.test_karim2.data.remote

data class ErrorMessage(
    var status: Boolean? = null,
    var statusCode: Int? = null,
    var message: String? = null,
    var reason: String? = null
)