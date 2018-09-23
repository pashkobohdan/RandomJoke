package com.pashkobohdan.randomjoke.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.pashkobohdan.randomjoke.data.model.rzhunemogu.JokeDTO

@Dao
interface JokesDAO {

    @get:Query("SELECT * FROM Joke")
    val allBookDtoList: List<JokeDTO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewJoke(vararg jokes: JokeDTO)
}