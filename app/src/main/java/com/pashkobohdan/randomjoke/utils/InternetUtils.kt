package com.pashkobohdan.randomjoke.utils

import android.content.Context
import android.net.ConnectivityManager

class InternetUtils(val context: Context) {

    fun isNetworkOn() :Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnectedOrConnecting
    }
}