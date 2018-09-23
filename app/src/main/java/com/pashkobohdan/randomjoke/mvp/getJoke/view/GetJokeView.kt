package com.pashkobohdan.randomjoke.mvp.getJoke.view

import com.pashkobohdan.randomjoke.data.model.enums.TopicType
import com.pashkobohdan.randomjoke.mvp.common.AbstractScreenView

interface GetJokeView :AbstractScreenView {

    fun setTopic(topicType: TopicType)

    fun showJoke(text: String)

    fun readJokeByTTS(text: String)

    fun showProgress()

    fun hideProgress()

    fun showOfflineMode(canUseSavedJokes: Boolean)

    fun hideOfflineMode()

    fun showLoadSavedJokesError()

    fun showSaveJokeError()

    fun showOfflineJokeLoadedInfo()

    fun showNoOfflineJokeLeftError()

    fun showNoOfflineJokeExistError()
}