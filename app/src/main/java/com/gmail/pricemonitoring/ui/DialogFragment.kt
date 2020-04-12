package com.gmail.pricemonitoring.ui

import android.R
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class DialogFragment(private val listener: IOnDialogBtnListener) : DialogFragment() {
    interface IOnDialogBtnListener {
        fun positivePressed(dialog: DialogInterface, which: Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity!!).apply {
            activity?.let {
                // Use the Builder class for convenient dialog construction
                this.setMessage("Все данные безвозвратно будут удалены, вы уверенны?")
                    .setPositiveButton("Да",
                        DialogInterface.OnClickListener { dialog, id ->
                            listener.positivePressed(dialog, id)
                        })
                    .setNegativeButton(R.string.cancel,
                        DialogInterface.OnClickListener { dialog, id ->
                            dialog.cancel()
                        })
            }
        }.create()
    }
}