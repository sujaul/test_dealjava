package com.test.test_karim2.Repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.test.test_karim2.data.local.AppDatabase
import com.test.test_karim2.data.model.*
import com.test.test_karim2.data.remote.ApiService
import com.test.test_karim2.data.remote.ErrorMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
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

    override suspend fun searchFilm(genre: String): Flow<List<Film>> {
        var data: Flow<List<Film>> = db.filmDAO().allByGenreFlow(genre)
        data.collect {
            if (it.isEmpty()){
                val response = service.search(genre).await()
                if (response.isSuccessful){
                    response.body()?.let {res ->
                        if (res.code == 200){
                            val filmList = ArrayList<Film>()
                            res.response.forEach {
                                val film = Film()
                                film.id = it._id
                                film.name = it.title
                                film.genre = genre
                                film.description = it.meta_description
                                film.url = it.poster.url
                                db.filmDAO().insertSus(film)
                                filmList.add(film)
                            }
                            data = flowOf(filmList)
                        } else throw Throwable(res.reason)
                    } ?: throw Throwable("The body is null")
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
        }
        return data
    }
}

interface globalRepository{
    suspend fun registerUser(data: Register) : ResponseParser

    suspend fun getUserToApi(page: Int, per_page: Int): Flow<List<Persons>>
    suspend fun getUserLocal(): Flow<List<Users>>?
    suspend fun getUserByUsernameAndPass(username: String, password: String): Flow<List<Users>>
    suspend fun getUserByUsername(username: String): Flow<List<Users>>
    suspend fun saveUser(username: String, password: String)
    suspend fun searchFilm(genre: String): Flow<List<Film>>
}