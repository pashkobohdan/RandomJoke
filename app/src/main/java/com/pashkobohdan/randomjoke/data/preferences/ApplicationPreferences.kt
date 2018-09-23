package com.pashkobohdan.randomjoke.data.preferences

import com.pashkobohdan.randomjoke.data.model.enums.TopicType

interface ApplicationPreferences {

    fun getCurrentTopic(): TopicType

    fun setCurrentTopic(topicType: TopicType)
}