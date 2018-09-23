package com.pashkobohdan.randomjoke.di

import com.pashkobohdan.randomjoke.di.modules.AppModule
import com.pashkobohdan.randomjoke.di.modules.ExecutionModule
import com.pashkobohdan.randomjoke.di.modules.NetworkModule
import com.pashkobohdan.randomjoke.ui.activities.GetJokeActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, AppModule::class, ExecutionModule::class])
@Singleton
interface ApplicationComponent {

    fun inject(activity: GetJokeActivity)
}