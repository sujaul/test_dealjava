package com.test.test_karim2.Repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.test.test_karim2.data.local.AppDatabase
import com.test.test_karim2.data.model.*
import com.test.test_karim2.data.remote.ApiService
import com.test.test_karim2.data.remote.ErrorMessage
import com.test.test_karim2.util.DateOperationUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GlobalRepositoryImpl(
    private val service: ApiService
): globalRepository, KoinComponent {

    private val db: AppDatabase by inject()

    override suspend fun registerUser(data: Register): ResponseParser {
        var res = ResponseParser()
        val response = service.register(data).await()
        if (response.isSuccessful){
            response.body()?.let {
                res = it
            }
        } else {
            if (response.errorBody()!=null) {
                val json: String = response.errorBody()!!.string()
                val obj: JsonObject = JsonParser().parse(json).asJsonObject
                val error = Gson().fromJson(obj, ErrorMessage::class.java)
                throw Throwable(error.message)
            } else {
                throw Throwable("The error body is null")
            }
        }
        return res
    }




    override suspend fun getUserToApi(page: Int, per_page: Int): Flow<List<Persons>> {
        var data: Flow<List<Persons>> = flowOf(arrayListOf())
        val response = service.getEvents(page, per_page).await()
        if (response.isSuccessful){
            response.body()?.let {
                it.data?.let {users ->
                    data = flowOf(users)
                }
            }
        } else {
            if (response.errorBody()!=null) {
                val json: String = response.errorBody()!!.string()
                val obj: JsonObject = JsonParser().parse(json).asJsonObject
                val error = Gson().fromJson(obj, ErrorMessage::class.java)
                throw Throwable(error.message)
            } else {
                throw Throwable("The error body is null")
            }
        }
        return data
    }

    override suspend fun getUserLocal(): Flow<List<Users>> {
        val allFlow = db.userDAO().allFlow()
        return allFlow
    }

    override suspend fun getUserByUsernameAndPass(
        username: String,
        password: String
    ): Flow<List<Users>> {
        return db.userDAO().allFlowByUsernameAndPass(username, password)
    }

    override suspend fun getUserByUsername(
        username: String): Flow<List<Users>> {
        return db.userDAO().allFlowByUsername(username)
    }

    override suspend fun saveUser(
        username: String,
        password: String
    ) {
        val user = Users()
        user.username = username
        user.password = password
        db.userDAO().insertSus(user)
    }

    override suspend fun searchFilm(genre: String): Flow<List<FilmAndFilmstokRelation>> {
        var data: Flow<List<FilmAndFilmstokRelation>> = db.filmDAO().filmAndFilmStokByGenre(genre)
        //var data: Flow<List<FilmAndFilmstokRelation>> = flowOf(dataList)
        val dataList = if (genre == "") db.filmDAO().allFilmAndFilmStokByGenreSus()
        else db.filmDAO().filmAndFilmStokByGenreSus(genre)
        if (dataList.isEmpty()){
            val input = if (genre == "") "action" else genre
            val response = service.search(input).await()
            if (response.isSuccessful){
                try {
                    response.body()?.let {res ->
                        if (res.code == 200){
                            val filmList = ArrayList<Film>()
                            res.response.forEach {
                                val film = Film()
                                film.id = it._id
                                film.name = it.title
                                film.genre = input
                                film.description = it.meta_description
                                film.url = "https:${it.poster.url}"
                                db.filmDAO().insertSus(film)
                                val filmStok = FilmStok()
                                filmStok.film_id = it._id
                                filmStok.stok_awal = 0
                                filmStok.credit = 1
                                filmStok.debit = 0
                                filmStok.stok_ahir = 1
                                filmStok.created_at = DateOperationUtil.getCurrentTimeStr("yyyy-MM-dd HH:mm:ss")
                                filmStok.updated_at = DateOperationUtil.getCurrentTimeStr("yyyy-MM-dd HH:mm:ss")
                                db.filmStokDAO().insertSus(filmStok)
                                filmList.add(film)
                            }
                            data = db.filmDAO().filmAndFilmStokByGenre(input)
                        } else throw Throwable(res.reason)
                    } ?: throw Throwable("The body is null")
                } catch (e: Exception){
                    throw Throwable(e.message)
                }
            } else {
                if (response.errorBody()!=null) {
                    val json: String = response.errorBody()!!.string()
                    val obj: JsonObject = JsonParser().parse(json).asJsonObject
                    val error = Gson().fromJson(obj, ErrorMessage::class.java)
                    throw Throwable(error.reason)
                } else {
                    throw Throwable("The error body is null")
                }
            }
        }
        return data
    }

    override suspend fun addStok(film: Film, stok: Int) {
        val filmStok = FilmStok()
        filmStok.film_id = film.id
        filmStok.stok_awal = stok
        filmStok.credit = 1
        filmStok.debit = 0
        filmStok.stok_ahir = stok+1
        filmStok.created_at = DateOperationUtil.getCurrentTimeStr("yyyy-MM-dd HH:mm:ss")
        filmStok.updated_at = DateOperationUtil.getCurrentTimeStr("yyyy-MM-dd HH:mm:ss")
        db.filmStokDAO().insertSus(filmStok)
    }

    override suspend fun minusStok(film: Film, stok: Int) {
        val filmStok = FilmStok()
        filmStok.film_id = film.id
        filmStok.stok_awal = stok
        filmStok.credit = 0
        filmStok.debit = 1
        filmStok.stok_ahir = stok-1
        filmStok.created_at = DateOperationUtil.getCurrentTimeStr("yyyy-MM-dd HH:mm:ss")
        filmStok.updated_at = DateOperationUtil.getCurrentTimeStr("yyyy-MM-dd HH:mm:ss")
        db.filmStokDAO().insertSus(filmStok)
    }
}

interface globalRepository{
    suspend fun registerUser(data: Register) : ResponseParser

    suspend fun getUserToApi(page: Int, per_page: Int): Flow<List<Persons>>
    suspend fun getUserLocal(): Flow<List<Users>>?
    suspend fun getUserByUsernameAndPass(username: String, password: String): Flow<List<Users>>
    suspend fun getUserByUsername(username: String): Flow<List<Users>>
    suspend fun saveUser(username: String, password: String)
    suspend fun searchFilm(genre: String): Flow<List<FilmAndFilmstokRelation>>
    suspend fun addStok(film: Film, stok: Int)
    suspend fun minusStok(film: Film, stok: Int)
}