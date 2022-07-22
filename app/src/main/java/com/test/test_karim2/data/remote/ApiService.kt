package com.test.test_karim2.data.remote

import com.test.test_karim2.data.model.BookResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    /**
     * API FOR LOGIN
     */
    @GET("volumes")
    fun search(
        @Query("q") tittle: String
    ): Deferred<Response<BookResponse>>

    /**
     * API FOR REGISTER
     */
   /* @FormUrlEncoded
    @POST("api/sign-up")
    fun register(
        @Body register: Register
    ): Deferred<Response<ResponseParser>>*/

}


