package com.pashkobohdan.randomjoke.utils

import android.app.AlertDialog
import android.content.Context
import com.pashkobohdan.randomjoke.R

//TODO rebuild to DialogUtils
object AlertUtils {

    fun showAlert(context: Context, text: String, okText: String = context.getString(R.string.ok), okCallback: () -> Unit = {}) {
        val builder1 = AlertDialog.Builder(context)
        builder1.setMessage(text)
        builder1.setCancelable(true)

        builder1.setPositiveButton(okText) { dialog, _ ->
            dialog.cancel()
            okCallback()
        }

//        builder1.setNegativeButton(
//                "No",
//                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

        val alert11 = builder1.create()
        alert11.show()
    }
}