package com.zzz.oss_rubbishcommunity.ui.fragment.managemachine

import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.zzz.oss_rubbishcommunity.R
import com.zzz.oss_rubbishcommunity.databinding.DialogAddMachineBinding
import com.zzz.oss_rubbishcommunity.model.api.machine.Machine
import com.zzz.oss_rubbishcommunity.ui.widget.showContentDialog

fun ManageMachineFragment.showManageMachineDialog(
        machineTemp:Machine? = null,
        onPositiveClick: ((DialogAddMachineBinding) -> Unit)? = null
) {
    val contentBinding = DataBindingUtil.inflate<DialogAddMachineBinding>(
            LayoutInflater.from(context),
            R.layout.dialog_add_machine,
            null,
            false
    ).apply {
        isAdd = (machine == null)
        this.machine = machineTemp
    }
    showContentDialog(
            context = context!!,
            title = "设备管理",
            contentBinding = contentBinding,
            onPositiveClick = {
                onPositiveClick?.invoke(contentBinding)
                true
            },
            onNegativeClick = { true }
    )

}