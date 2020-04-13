package com.zzz.oss_rubbishcommunity.ui.fragment.manageuser

import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter
import com.zzz.oss_rubbishcommunity.R
import com.zzz.oss_rubbishcommunity.databinding.CellUserBinding
import com.zzz.oss_rubbishcommunity.model.api.SimpleProfileResp

class UserListAdapter(
    onCellClick: (SimpleProfileResp) -> Unit
) : BaseRecyclerAdapter<SimpleProfileResp, CellUserBinding>(
    R.layout.cell_user,
    onCellClick
) {
    override fun bindData(binding: CellUserBinding, position: Int) {
        binding.profile = baseList[position]
    }
}