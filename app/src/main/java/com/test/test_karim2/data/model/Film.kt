package com.test.test_karim2.data.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "film", primaryKeys = ["id"])
data class Film(
        @ColumnInfo(name = "id")
        var id: String,
        @ColumnInfo(name = "name")
        var name: String = "",
        @ColumnInfo(name = "genre")
        var genre: String = "",
        @ColumnInfo(name = "description")
        var description: String = "",
        @ColumnInfo(name = "url")
        var url: String = ""
){
    @Ignore constructor() : this("")
}

@Dao
interface FilmDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSus(data: Film)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLists(lists: List<Film>)

    @Query("SELECT * FROM film WHERE id = :id ORDER BY genre DESC")
    fun allByIdFlow(id : String): Flow<List<Film>>

    @Query("SELECT * FROM film WHERE genre = :genre ORDER BY name DESC")
    fun allByGenreFlow(genre : String): Flow<List<Film>>

    @Query("SELECT * FROM film ORDER BY genre DESC")
    fun allFlow(): Flow<List<Film>>

    @Query("DELETE FROM film")
    suspend fun deleteAll()

    @Query("DELETE FROM film WHERE id = :id")
    suspend fun deleteByIdSus(id : Int)
}
