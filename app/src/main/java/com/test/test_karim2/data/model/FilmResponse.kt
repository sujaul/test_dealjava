package com.test.test_karim2.data.model

data class FilmResponse(
        var code: Int = 0,
        var response: List<FilmParser>,
        var reason: String? = null
)
