package com.pashkobohdan.randomjoke.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.pashkobohdan.randomjoke.data.database.dao.JokesDAO
import com.pashkobohdan.randomjoke.data.model.rzhunemogu.JokeDTO

@Database(entities = [JokeDTO::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getJokesDAO(): JokesDAO
}