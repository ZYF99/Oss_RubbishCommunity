package com.zzz.oss_rubbishcommunity.ui.widget

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import com.zzz.oss_rubbishcommunity.R

fun showContentDialog(
        context: Context,
        contentBinding: ViewDataBinding,
        title: String = "",
        @StringRes positiveButtonText: Int = R.string.dialog_ok,
        @StringRes negativeButtonText: Int = R.string.dialog_button_cancel,
        onPositiveClick: () -> Boolean,
        onNegativeClick: () -> Boolean = { true }
) {
    val alertDialog = AlertDialog.Builder(context)
        .setView(
            contentBinding.root
        )
        .setTitle(title)
        .setPositiveButton(positiveButtonText, null)
        .setNegativeButton(negativeButtonText, null)
        .create()
    alertDialog.show()
    //on positiveClick
    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
        if (onPositiveClick()) alertDialog.dismiss() //dismiss dialog when action return true
    }
    //on negativeClick
    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
        if (onNegativeClick.invoke()) alertDialog.dismiss() //dismiss dialog when action return true
    }
}

fun showContentDialog(
    context: Context,
    contentBinding: ViewDataBinding,
    @StringRes title: Int = R.string.dialog_title,
    @StringRes positiveButtonText: Int = R.string.dialog_ok,
    @StringRes negativeButtonText: Int = R.string.dialog_button_cancel,
    onPositiveClick: () -> Boolean,
    onNegativeClick: () -> Boolean = { true }
) = showContentDialog(
    context,
    contentBinding,
    context.getString(title),
    positiveButtonText,
    negativeButtonText,
    onPositiveClick,
    onNegativeClick
)