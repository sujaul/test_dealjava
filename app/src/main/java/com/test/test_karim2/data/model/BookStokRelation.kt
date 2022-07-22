package com.test.test_karim2.data.model

import androidx.room.DatabaseView
import androidx.room.Embedded

@DatabaseView(
    value = """
        SELECT book.*, book_stok.id as book_stok_id, 
        book_stok.book_id as book_stok_bookid, 
        book_stok.stok_awal as book_stok_stokawal, 
        book_stok.stok_ahir as book_stok_stokahir, 
        book_stok.created_at as book_stok_createdat
        FROM book
        LEFT JOIN book_stok ON ( book_stok.book_id = book.id )
        WHERE book_stok.id IN ( SELECT MAX(id) 
        FROM book_stok
        GROUP BY book_stok.book_id )""")
data class BookStokRelation(
    @Embedded
    var books : Book,
    var book_stok_bookid: String? = null,
    var book_stok_stokawal: Int? = null,
    var book_stok_stokahir: Int? = null,
    var book_stok_createdat: String? = null,
    var book_stok_id: Int? = null
)
