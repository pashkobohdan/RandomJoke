package com.pashkobohdan.randomjoke.di.modules

import android.arch.persistence.room.Room
import android.content.Context
import com.pashkobohdan.randomjoke.data.database.AppDatabase
import com.pashkobohdan.randomjoke.data.preferences.ApplicationPreferences
import com.pashkobohdan.randomjoke.data.preferences.impl.PreferencesHolder
import com.pashkobohdan.randomjoke.utils.InternetUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton



@Module
class AppModule(val context: Context) {

    private val preferencesHolder: PreferencesHolder by lazy {
        PreferencesHolder(context)
    }

    @Provides
    @Singleton
    fun provideApplicationPreferences(): ApplicationPreferences = preferencesHolder

    @Provides
    @Singleton
    fun provideInternetUtils(): InternetUtils = InternetUtils(context)

    @Provides
    @Singleton
    fun provideAppDatabase(): AppDatabase {
       return Room.databaseBuilder(context, AppDatabase::class.java,
                "book_database").build()
    }
}