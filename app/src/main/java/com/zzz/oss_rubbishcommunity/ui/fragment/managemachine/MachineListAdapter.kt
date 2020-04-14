package com.zzz.oss_rubbishcommunity.ui.fragment.managemachine

import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter
import com.zzz.oss_rubbishcommunity.R
import com.zzz.oss_rubbishcommunity.databinding.CellMachineBinding
import com.zzz.oss_rubbishcommunity.model.api.machine.Machine

class MachineListAdapter(
    onCellClick: (Machine) -> Unit
) : BaseRecyclerAdapter<Machine, CellMachineBinding>(
    R.layout.cell_user,
    onCellClick
) {
    override fun bindData(binding: CellMachineBinding, position: Int) {
        binding.machine = baseList[position]
    }
}