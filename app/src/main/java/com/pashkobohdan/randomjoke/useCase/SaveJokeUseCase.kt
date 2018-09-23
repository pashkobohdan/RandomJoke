package com.pashkobohdan.randomjoke.useCase

import com.pashkobohdan.randomjoke.data.database.AppDatabase
import com.pashkobohdan.randomjoke.data.model.rzhunemogu.JokeDTO
import com.pashkobohdan.randomjoke.execution.RunUseCase
import javax.inject.Inject

class SaveJokeUseCase @Inject constructor(): RunUseCase<JokeDTO>() {

    @Inject
    lateinit var appDatabase: AppDatabase

    override fun justDoThis(request: JokeDTO) {
        appDatabase.getJokesDAO().insertNewJoke(request)
    }
}