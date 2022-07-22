package com.test.test_karim2.data.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "book_stok")
data class BookStok(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Int,
        @ColumnInfo(name = "book_id")
        var book_id: String = "",
        @ColumnInfo(name = "stok_awal")
        var stok_awal: Int = 0,
        @ColumnInfo(name = "debit") // pengurangan
        var debit: Int = 0,
        @ColumnInfo(name = "credit")
        var credit: Int = 0, // penambahan
        @ColumnInfo(name = "stok_ahir")
        var stok_ahir: Int = 0,
        @ColumnInfo(name = "created_at")
        var created_at: String = "",
        @ColumnInfo(name = "updated_at")
        var updated_at: String = ""
){
    @Ignore constructor() : this(0)
    @Ignore
    var film : Book? = null
}

@Dao
interface BookStokDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSus(data: BookStok)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLists(lists: List<BookStok>)

    @Query("SELECT * FROM book_stok WHERE id = :id ORDER BY updated_at DESC")
    fun allByIdFlow(id : Int): Flow<List<BookStok>>

    @Query("SELECT * FROM book_stok WHERE book_id = :book_id AND credit != 0 ORDER BY updated_at DESC")
    suspend fun allByBookIdCredit(book_id : String): List<BookStok>

    @Query("SELECT * FROM book_stok WHERE book_id = :book_id AND debit != 0 ORDER BY updated_at DESC")
    suspend fun oneByBookIdDebit(book_id : String): BookStok?

    @Query("SELECT * FROM book_stok ORDER BY updated_at DESC")
    fun allFlow(): Flow<List<BookStok>>

    @Query("DELETE FROM book_stok WHERE id = :id")
    suspend fun deleteByIdSus(id : Int)
}
