package com.pashkobohdan.randomjoke.useCase

import com.pashkobohdan.randomjoke.data.database.AppDatabase
import com.pashkobohdan.randomjoke.data.model.rzhunemogu.JokeDTO
import com.pashkobohdan.randomjoke.execution.GetUseCase
import javax.inject.Inject

class GetJokesUseCase @Inject constructor(): GetUseCase<List<JokeDTO>>() {

    @Inject
    lateinit var appDatabase: AppDatabase

    override fun getData(): List<JokeDTO> {
        return appDatabase.getJokesDAO().allBookDtoList
    }
}