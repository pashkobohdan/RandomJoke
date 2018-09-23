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
                TopicType.ALL -> getString(R.string.all_topics)
                TopicType.ANECDOT -> getString(R.string.topic_anecdot)
                TopicType.RASSKAZ -> getString(R.string.topic_rasskaz)
                TopicType.STISHKI -> getString(R.string.topic_stishki)
                TopicType.AFORIZMI -> getString(R.string.topic_aforizmi)
                TopicType.CITATI -> getString(R.string.topic_citaty)
                TopicType.TOSTY -> getString(R.string.topic_tosty)
                TopicType.STATUSY -> getString(R.string.topic_statusy)
                TopicType.ANECDOT_18 ->getString(R.string.topic_anecdot_18)
                TopicType.RASSKAZ_18 ->getString(R.string.topic_rasskaz_18)
                TopicType.STISHKI_18 ->getString(R.string.topic_stishki_18)
                TopicType.AFORIZMI_18 ->getString(R.string.topic_aforizmi_18)
                TopicType.CITATI_18 ->getString(R.string.topic_citaty_18)
                TopicType.TOSTY_18 ->getString(R.string.topic_tosty_18)
                TopicType.STATUSY_18 ->getString(R.string.topic_statusy_18)
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
        val text = getString(R.string.no_internet_connection) + " " +
                (if (canUseSavedJokes) getString(R.string.data_will_be_loaded_from_storage) else "") +" " +
                getString(R.string.for_reconnect_click_icon)
        AlertUtils.showAlert(this, text) {
            disconnectedButton.visibility = View.VISIBLE
        }
    }

    override fun hideOfflineMode() {
        offlineMode = false
        Snackbar.make(root, getString(R.string.connection_fixed), Snackbar.LENGTH_SHORT).show()
        disconnectedButton.visibility = View.GONE
    }

    override fun showLoadSavedJokesError() {
        Snackbar.make(root, getString(R.string.local_storage_load_error), Snackbar.LENGTH_SHORT).show()
    }

    override fun showSaveJokeError() {
        Snackbar.make(root, getString(R.string.local_storage_save_error), Snackbar.LENGTH_SHORT).show()
    }

    override fun showOfflineJokeLoadedInfo() {
        Snackbar.make(root, getString(R.string.data_loaded_from_local_storage), Snackbar.LENGTH_SHORT).show()
    }

    override fun showNoOfflineJokeLeftError() {
        AlertUtils.showAlert(this, getString(R.string.ask_for_shuffle_local_data_again)) {
            presenter.reuseOfflineJokes()
        }
    }

    override fun showNoOfflineJokeExistError() {
        AlertUtils.showAlert(this, getString(R.string.no_local_data_error))
    }
}
