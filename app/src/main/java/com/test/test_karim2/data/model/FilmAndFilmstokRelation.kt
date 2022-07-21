package com.test.test_karim2.data.model

import androidx.room.Dao
import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Query

@DatabaseView(
    value = """
        SELECT film.*, film_stok.id as stok_id, film_stok.stok_ahir as film_stok_ahir, film_stok.created_at as film_stok_created
        FROM film_stok
        LEFT JOIN film ON (film_stok.film_id = film.id) 
        GROUP BY film_stok.film_id
        ORDER BY film_stok.id DESC""")
data class FilmAndFilmstokRelation(
    @Embedded
    var films : Film,
    var film_stok_ahir: Int,
    var film_stok_created: String,
    var stok_id: Int
)
