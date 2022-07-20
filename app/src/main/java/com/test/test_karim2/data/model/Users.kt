package com.test.test_karim2.data.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

//@Entity(tableName = "users", primaryKeys = ["id"])
@Entity(tableName = "users")
data class Users(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Int,
        @ColumnInfo(name = "username")
        var username: String = "",
        @ColumnInfo(name = "password")
        var password: String = ""
){
    @Ignore constructor() : this(0)
}

@Dao
interface UsersDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSus(data: Users)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLists(lists: List<Users>)

    @Query("SELECT * FROM users WHERE id = :id ORDER BY username DESC")
    fun allByIdFlow(id : Int): Flow<List<Users>>

    @Query("SELECT * FROM users ORDER BY username DESC")
    fun allFlow(): Flow<List<Users>>

    @Query("SELECT * FROM users WHERE username = :username AND password = :password ORDER BY username DESC")
    fun allFlowByUsernameAndPass(username: String, password: String): Flow<List<Users>>

    @Query("SELECT * FROM users WHERE username = :username ORDER BY username DESC")
    fun allFlowByUsername(username: String): Flow<List<Users>>

    @Query("DELETE FROM users")
    suspend fun deleteAll()

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteByIdSus(id : Int)
}
