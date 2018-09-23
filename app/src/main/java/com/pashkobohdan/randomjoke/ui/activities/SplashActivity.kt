package com.pashkobohdan.randomjoke.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.pashkobohdan.randomjoke.R

class SplashActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({
            val intent = Intent(this, GetJokeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }, SPLASH_DELAY)
    }

    companion object {
        private const val SPLASH_DELAY = 500L
    }
}