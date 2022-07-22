package com.test.test_karim2.data.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "book", primaryKeys = ["id"])
data class Book(
        @ColumnInfo(name = "id")
        var id: String,
        @ColumnInfo(name = "name")
        var name: String = "",
        @ColumnInfo(name = "genre")
        var genre: String = "",
        @ColumnInfo(name = "author")
        var author: String = "",
        @ColumnInfo(name = "description")
        var description: String = "",
        @ColumnInfo(name = "publisher")
        var publisher: String = "",
        @ColumnInfo(name = "published_date")
        var published_date: String = "",
        @ColumnInfo(name = "qty")
        var qty: Int = 0,
        @ColumnInfo(name = "url")
        var url: String = ""
){
    @Ignore constructor() : this("")
}

@Dao
interface BookDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSus(data: Book)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLists(lists: List<Book>)

    @Query("DELETE FROM book")
    suspend fun deleteAll()

    @Query("DELETE FROM book WHERE id = :id")
    suspend fun deleteByIdSus(id : Int)

    @Query(
        """SELECT * FROM BookStokRelation WHERE 
        BookStokRelation.name LIKE :name or 
        BookStokRelation.name = :name 
        ORDER BY BookStokRelation.name DESC"""
    )
    fun filmAndFilmStokByGenre(name : String): Flow<List<BookStokRelation>>

    @Query(
        """SELECT * FROM BookStokRelation WHERE 
        BookStokRelation.name LIKE :name or 
        BookStokRelation.name = :name 
        ORDER BY BookStokRelation.name DESC"""
    )
    suspend fun filmAndFilmStokByGenreSus(name : String): List<BookStokRelation>
}
