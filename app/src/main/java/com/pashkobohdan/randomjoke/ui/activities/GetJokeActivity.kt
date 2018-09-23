package com.pashkobohdan.randomjoke.ui.activities

import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.design.widget.Snackbar
import android.transition.AutoTransition
import android.transition.Transition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.pashkobohdan.randomjoke.R
import com.pashkobohdan.randomjoke.RandomJokeApplication
import com.pashkobohdan.randomjoke.data.model.enums.TopicType
import com.pashkobohdan.randomjoke.mvp.getJoke.GetJokePresenter
import com.pashkobohdan.randomjoke.mvp.getJoke.view.GetJokeView
import com.pashkobohdan.randomjoke.ui.common.AbstractActivity
import com.pashkobohdan.randomjoke.ui.listeners.EmptyOnItemSelectedListener
import com.pashkobohdan.randomjoke.ui.listeners.EmptyTransitionListener
import com.pashkobohdan.randomjoke.utils.AlertUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_reading.jokeText
import java.util.*


class GetJokeActivity : AbstractActivity<GetJokePresenter>(), GetJokeView {

    @InjectPresenter
    lateinit var presenter: GetJokePresenter

    private var jokeTextMode = false
    private var canUseTTS = false
    private var offlineMode = false

    private val tts: TextToSpeech by lazy {
        TextToSpeech(this, TextToSpeech.OnInitListener { status ->
            canUseTTS = status == TextToSpeech.SUCCESS
        })
    }

    @ProvidePresenter
    fun createPresenter(): GetJokePresenter = presenterProvider.get()

    val topicList: List<TopicType> by lazy { TopicType.values().asList() }

    override fun onCreate(savedInstanceState: Bundle?) {
        RandomJokeApplication.INSTANSE.applicationComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getOneMoreJoke.setOnClickListener {
            presenter.loadOneMoreJoke()
        }
        readWithTTS.setOnClickListener {
            presenter.readCurrentJokeWithTTS()
        }
        disconnectedButton.setOnClickListener {
            presenter.checkConnection(true)
        }

        initTTS()
        initTopicTypeList()
    }

    override fun onPause() {
        super.onPause()
        tts.stop()
        tts.shutdown()
    }

    private fun initTTS() {
        tts.language = Locale("ru")//Only russian language jokes yet
    }

    private fun initTopicTypeList() {

        spinner.onItemSelectedListener = object : EmptyOnItemSelectedListener() {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.currentTopicType = TopicType.values()[position]
            }
        }

        fun getTopicNameByCode(topicType: TopicType): String {
            return when (topicType) {
                TopicType.ALL -> "Все подряд"
                TopicType.ANECDOT -> "Анекдот"
                TopicType.RASSKAZ -> "Рассказы"
                TopicType.STISHKI -> "Стишки"
                TopicType.AFORIZMI -> "Афоризмы"
                TopicType.CITATI -> "Цитаты"
                TopicType.TOSTY -> "Тосты"
                TopicType.STATUSY -> "Статусы"
                TopicType.ANECDOT_18 -> "Анекдот (+18)"
                TopicType.RASSKAZ_18 -> "Рассказы (+18)"
                TopicType.STISHKI_18 -> "Стишки (+18)"
                TopicType.AFORIZMI_18 -> "Афоризмы (+18)"
                TopicType.CITATI_18 -> "Цитаты (+18)"
                TopicType.TOSTY_18 -> "Тосты (+18)"
                TopicType.STATUSY_18 -> "Статусы (+18)"
            }
        }

        val topicNameList = topicList.map { getTopicNameByCode(it) }

        val dataAdapter = ArrayAdapter(this, R.layout.spinner_item, topicNameList)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = dataAdapter
    }

    override fun setTopic(topicType: TopicType) {
        spinner.setSelection(topicList.indexOf(topicType))
    }

    override fun showJoke(text: String) {
        if (!jokeTextMode) {
            turnOnJokeTextMode {
                if (canUseTTS) readWithTTS.visibility = View.VISIBLE
                if (offlineMode) disconnectedButton.visibility = View.VISIBLE
            }
            jokeTextMode = true
        } else {
            if (canUseTTS) readWithTTS.visibility = View.VISIBLE
        }
        jokeText.text = text
    }

    private fun turnOnJokeTextMode(onEnd: () -> Unit) {
        val constraint2 = ConstraintSet()
        val newConstraintLayout = LayoutInflater.from(this).inflate(R.layout.activity_main_reading, null as ViewGroup?) as ConstraintLayout
        constraint2.clone(newConstraintLayout)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val transition = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                AutoTransition()
            } else {
                throw IllegalStateException("SDK version in low than KITKAT")
            }

            transition.startDelay = 10
            transition.duration = 400
            transition.addListener(object : EmptyTransitionListener() {
                override fun onTransitionEnd(transition: Transition?) {
                    onEnd()
                }
            })

            TransitionManager.beginDelayedTransition(
                    root, transition)
        }
        constraint2.applyTo(root)
    }

    override fun readJokeByTTS(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null)
    }

    override fun showProgress() {
        waiter.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        waiter.visibility = View.INVISIBLE
    }

    override fun showOfflineMode(canUseSavedJokes: Boolean) {
        offlineMode = true
        val text = "Отсутствует подключение к интернету. " +
                (if (canUseSavedJokes) "Будут загружены уже сохраненные локально данные. " else "") +
                "Чтобы попытаться переподключиться - нажмите на появившийся значек, находящийся в нижнем левом углу"
        AlertUtils.showAlert(this, text) {
            disconnectedButton.visibility = View.VISIBLE
        }
    }

    override fun hideOfflineMode() {
        offlineMode = false
        Snackbar.make(root, "Связь с сетью восстановлена", Snackbar.LENGTH_SHORT).show()
        disconnectedButton.visibility = View.GONE
    }

    override fun showLoadSavedJokesError() {
        Snackbar.make(root, "Ошибка загрузки ранее сохраненных данных", Snackbar.LENGTH_SHORT).show()
    }

    override fun showSaveJokeError() {
        Snackbar.make(root, "Ошибка сохранения данных в локальное хранилище", Snackbar.LENGTH_SHORT).show()
    }

    override fun showOfflineJokeLoadedInfo() {
        Snackbar.make(root, "Загружены данные с локального хранилища", Snackbar.LENGTH_SHORT).show()
    }

    override fun showNoOfflineJokeLeftError() {
        AlertUtils.showAlert(this, "Не осталось данных сохраненных в локальном хранилище. " +
                "Перетасовать и использовать снова ?") {
            presenter.reuseOfflineJokes()
        }
    }

    override fun showNoOfflineJokeExistError() {
        AlertUtils.showAlert(this, "На устройстве еще нету сохраненный данных. " +
                "Нужно подключение к интернету")
    }
}
