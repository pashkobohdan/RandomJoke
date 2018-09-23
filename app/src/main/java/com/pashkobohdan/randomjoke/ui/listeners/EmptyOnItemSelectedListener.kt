package com.pashkobohdan.randomjoke.ui.listeners

import android.view.View
import android.widget.AdapterView

open class EmptyOnItemSelectedListener : AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
        //nop
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //nop
    }
}