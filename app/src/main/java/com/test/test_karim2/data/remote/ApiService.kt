package com.test.test_karim2.data.remote

import com.test.test_karim2.data.model.FilmResponse
import com.test.test_karim2.data.model.Register
import com.test.test_karim2.data.model.ResponseParser
import com.test.test_karim2.data.model.Users
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    /**
     * API FOR LOGIN
     */
    @GET("vod/search")
    fun search(
        @Field("genre") genre: String
    ): Deferred<Response<FilmResponse>>

    /**
     * API FOR REGISTER
     */
    @FormUrlEncoded
    @POST("api/sign-up")
    fun register(
        @Body register: Register
    ): Deferred<Response<ResponseParser>>


    /**
     * API FOR SEARCH USER
     */
    //@Headers("Accept: application/json")
    @GET("api/users")
    fun getEvents(@Query("page") page: Int,
                  @Query("per_page") per_page: Int): Deferred<Response<GuestResponse>>

    /**
     * API FOR SEARCH USER
     */
    //@Headers("Accept: application/json")
    @GET("users/{user}/repos")
    fun getRepo(
        @Path("user") user: String
    ): Deferred<Response<List<Users>>>
}


