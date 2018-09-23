package com.pashkobohdan.randomjoke.data.preferences.impl

import android.content.Context
import android.content.SharedPreferences
import com.pashkobohdan.randomjoke.data.model.enums.TopicType
import com.pashkobohdan.randomjoke.data.preferences.ApplicationPreferences

class PreferencesHolder(val context: Context) : ApplicationPreferences {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    override fun getCurrentTopic(): TopicType {
        return TopicType.findByCode(sharedPreferences.getInt(ATTR_TOPIC_NAME, 0))
    }

    override fun setCurrentTopic(topicType: TopicType) {
        sharedPreferences.edit().putInt(ATTR_TOPIC_NAME, topicType.code).apply()
    }

    companion object {
        private const val PREFERENCES_NAME = "appPreferences"

        //properties
        private const val ATTR_TOPIC_NAME = "appPreferences"
    }
}