package com.pashkobohdan.randomjoke.di.modules

import com.google.gson.GsonBuilder
import com.pashkobohdan.randomjoke.data.model.enums.JokeSource
import com.pashkobohdan.randomjoke.data.retrofit.RzhunemoguJokesApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton




@Module
class NetworkModule {

    private val retrofit: Retrofit by lazy {
        val gson = GsonBuilder()
                .setLenient()
                .create()

        Retrofit.Builder()
                .baseUrl(JokeSource.RZHUNEMOGU.rootUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private val rzhunemoguApi: RzhunemoguJokesApi by lazy {
        retrofit.create(RzhunemoguJokesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRzhunemoguApi(): RzhunemoguJokesApi = rzhunemoguApi
}