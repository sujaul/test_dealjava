package com.test.test_karim2.data.model

data class FilmParser(
        var _id: String = "",
        var title: String = "",
        var meta_description: String = "",
        var poster: FilmPoster,
        var total: Int,
        var total_found: Int
)
