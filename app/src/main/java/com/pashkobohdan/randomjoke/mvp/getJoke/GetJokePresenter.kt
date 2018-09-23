package com.pashkobohdan.randomjoke.mvp.getJoke

import com.arellomobile.mvp.InjectViewState
import com.google.gson.stream.MalformedJsonException
import com.pashkobohdan.randomjoke.data.model.enums.TopicType
import com.pashkobohdan.randomjoke.data.model.rzhunemogu.JokeDTO
import com.pashkobohdan.randomjoke.data.preferences.ApplicationPreferences
import com.pashkobohdan.randomjoke.data.retrofit.RzhunemoguJokesApi
import com.pashkobohdan.randomjoke.execution.observers.EmptyObserver
import com.pashkobohdan.randomjoke.execution.observers.ErrorObserver
import com.pashkobohdan.randomjoke.mvp.common.AbstractPresenter
import com.pashkobohdan.randomjoke.mvp.getJoke.view.GetJokeView
import com.pashkobohdan.randomjoke.useCase.GetJokesUseCase
import com.pashkobohdan.randomjoke.useCase.SaveJokeUseCase
import com.pashkobohdan.randomjoke.utils.InternetUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class GetJokePresenter @Inject constructor() : AbstractPresenter<GetJokeView>() {

    @Inject
    lateinit var rzhunemoguApi: RzhunemoguJokesApi
    @Inject
    lateinit var internetUtils: InternetUtils
    @Inject
    lateinit var preferences: ApplicationPreferences
    @Inject
    lateinit var getJokesUseCase: GetJokesUseCase
    @Inject
    lateinit var saveJokeUseCase: SaveJokeUseCase

    private lateinit var currentJoke: JokeDTO
    var currentTopicType: TopicType = TopicType.ALL
        set(value) {
            if (value != field) {
                preferences.setCurrentTopic(value)
                viewState.setTopic(value)
                field = value
            }
        }

    private var offlineMode = false
    private var offlineSavedJokes: MutableList<JokeDTO> = mutableListOf()
    private var offlineSavedShuffledJokes: MutableList<JokeDTO> = mutableListOf()

    override fun onFirstViewAttach() {
        viewState.showProgress()

        currentTopicType = preferences.getCurrentTopic()

        getJokesUseCase.execute(object : EmptyObserver<List<JokeDTO>>() {
            override fun onNext(list: List<JokeDTO>) {
                val shuffledList = list.shuffled()
                offlineSavedJokes = shuffledList.toMutableList()
                offlineSavedShuffledJokes = shuffledList.toMutableList()
            }

            override fun onError(e: Throwable) {
                viewState.showLoadSavedJokesError()
            }

            override fun onFinally() {
                viewState.hideProgress()
                checkConnection()
            }
        })
    }

    fun reuseOfflineJokes(){
        offlineSavedShuffledJokes = offlineSavedJokes.shuffled().toMutableList()
        loadOneMoreJoke()
    }

    fun checkConnection(reconnectMode : Boolean= false) {
        if (internetUtils.isNetworkOn()) {
            offlineMode = false
            if(reconnectMode) {
                viewState.hideOfflineMode()
            }
        } else {
            offlineMode = true
            viewState.showOfflineMode(offlineSavedShuffledJokes.isNotEmpty())
        }
    }

    private fun loadSavedOfflineJoke() {
        if (offlineSavedShuffledJokes.isEmpty()) {
            if(offlineSavedJokes.isEmpty()) {
                viewState.showNoOfflineJokeExistError()
            } else {
                viewState.showNoOfflineJokeLeftError()
            }
        } else {
            val nextShuffledOfflineJoke = offlineSavedShuffledJokes[0]
            offlineSavedShuffledJokes.remove(nextShuffledOfflineJoke)

            currentJoke = nextShuffledOfflineJoke
            viewState.hideProgress()
            viewState.showJoke(nextShuffledOfflineJoke.content)

            viewState.showOfflineJokeLoadedInfo()
        }
    }

    fun loadOneMoreJoke() {
        if (offlineMode) {
            loadSavedOfflineJoke()
        } else {
            viewState.showProgress()
            startLoadingNextJoke({ loadedJoke ->
                saveNewJoke(loadedJoke)
                currentJoke = loadedJoke
                viewState.hideProgress()
                viewState.showJoke(loadedJoke.content)
            }, {
                offlineMode = true
                viewState.showOfflineMode(offlineSavedShuffledJokes.isNotEmpty())
                viewState.hideProgress()
            })
        }
    }

    private fun startLoadingNextJoke(onSuccess: (JokeDTO) -> Unit, onFailure: () -> Unit) {
        val observable = if (currentTopicType == TopicType.ALL) {
            rzhunemoguApi.getRandomJoke()
        } else {
            rzhunemoguApi.getRandomJokeByTopic(currentTopicType.code)
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(LOAD_JOKE_TIMEOUT, TimeUnit.MILLISECONDS)
                .delay(LOAD_JOKE_DELAY, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe({ loadedJoke ->
                    if (loadedJoke == null) {
                        onFailure()
                        return@subscribe
                    }
                    onSuccess(loadedJoke)
                }, { error ->
                    if (error is MalformedJsonException?) {
                        startLoadingNextJoke(onSuccess, onFailure)
                    } else {
                        onFailure()
                    }
                })
    }

    fun readCurrentJokeWithTTS() {
        viewState.readJokeByTTS(currentJoke.content)
    }

    private fun saveNewJoke(jokeDTO: JokeDTO) {
        saveJokeUseCase.execute(jokeDTO, ErrorObserver {
            viewState.showSaveJokeError()
        })
    }

    companion object {
        private const val LOAD_JOKE_DELAY = 150L
        private const val LOAD_JOKE_TIMEOUT= 5000L
    }
}