package com.pashkobohdan.randomjoke.data.model.rzhunemogu

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Joke")
class JokeDTO {

    @PrimaryKey
    var id: Long = System.currentTimeMillis()

    @SerializedName("content")
    @Expose
    lateinit var content: String
}