package com.pashkobohdan.randomjoke.ui.listeners

import android.transition.Transition

open class EmptyTransitionListener : Transition.TransitionListener {
    override fun onTransitionEnd(transition: Transition?) {
        //nop
    }

    override fun onTransitionResume(transition: Transition?) {
        //nop
    }

    override fun onTransitionPause(transition: Transition?) {
        //nop
    }

    override fun onTransitionCancel(transition: Transition?) {
        //nop
    }

    override fun onTransitionStart(transition: Transition?) {
        //nop
    }
}