package com.test.test_karim2.data.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "film_stok")
data class FilmStok(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Int,
        @ColumnInfo(name = "film_id")
        var film_id: String = "",
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
    var film : Film? = null
}

@Dao
interface FilmStokDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSus(data: FilmStok)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLists(lists: List<FilmStok>)

    @Query("SELECT * FROM film_stok WHERE id = :id ORDER BY updated_at DESC")
    fun allByIdFlow(id : Int): Flow<List<FilmStok>>

    @Query("SELECT * FROM film_stok WHERE film_id = :film_id ORDER BY updated_at ASC")
    suspend fun allByIdFilmFlow(film_id : String): List<FilmStok>

    @Query("SELECT * FROM film_stok ORDER BY updated_at DESC")
    fun allFlow(): Flow<List<FilmStok>>

    @Query("DELETE FROM film_stok WHERE id = :id")
    suspend fun deleteByIdSus(id : Int)
}
