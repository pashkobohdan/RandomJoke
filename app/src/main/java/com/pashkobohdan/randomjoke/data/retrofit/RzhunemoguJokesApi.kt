package com.pashkobohdan.randomjoke.data.retrofit

import com.pashkobohdan.randomjoke.data.model.rzhunemogu.JokeDTO
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RzhunemoguJokesApi {

    @GET("/RandJSON.aspx")
    fun getRandomJokeByTopic(@Query("CType") category: Int): Observable<JokeDTO>

    @GET("/RandJSON.aspx")
    fun getRandomJoke(): Observable<JokeDTO>
}