package com.zzz.oss_rubbishcommunity.ui.base

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import com.zzz.oss_rubbishcommunity.R
import com.zzz.oss_rubbishcommunity.util.DialogUtil


data class DialogEvent(
        @StringRes val title: Int = R.string.dialog_title,
        @StringRes val msg: Int = R.string.dialog_msg,
        @StringRes val positiveButtonText: Int = R.string.dialog_ok,
        @StringRes val negativeButtonText: Int = R.string.dialog_cancel,
        @DialogUtil.ButtonType val positiveButton: Long = DialogUtil.BUTTON_TYPE_OK,
        @DialogUtil.ButtonType val negativeButton: Long = DialogUtil.BUTTON_TYPE_CANCEL
)

class DialogLiveEvent : LiveEvent<Event<DialogEvent>>()

fun DialogLiveEvent.bindDialog(context: Context, owner: LifecycleOwner) {
    observe(owner) { event ->
        if (event.hasBeenHandled) return@observe
        val content = event.peekContent()
        DialogUtil.showDialogSingle(
            context,
            content.title,
            content.msg,
            content.positiveButtonText,
            content.negativeButtonText,
            content.positiveButton,
            content.negativeButton
        )
    }
}