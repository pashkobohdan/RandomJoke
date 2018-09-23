package com.pashkobohdan.randomjoke

import android.app.Application
import com.pashkobohdan.randomjoke.di.ApplicationComponent
import com.pashkobohdan.randomjoke.di.DaggerApplicationComponent
import com.pashkobohdan.randomjoke.di.modules.AppModule


class RandomJokeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANSE = this
    }

    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    companion object {
        lateinit var INSTANSE: RandomJokeApplication
    }
}