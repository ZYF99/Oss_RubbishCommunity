package com.zzz.oss_rubbishcommunity.ui.fragment.managemachine

import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.zzz.oss_rubbishcommunity.R
import com.zzz.oss_rubbishcommunity.databinding.FragmentManageMachineBinding
import com.zzz.oss_rubbishcommunity.ui.base.BaseFragment

class ManageMachineFragment : BaseFragment<FragmentManageMachineBinding, ManageMachineViewModel>(
    ManageMachineViewModel::class.java, R.layout.fragment_manage_machine
) {



    override fun initView() {
        viewModel.isRefreshing.observeNonNull {
            binding.refreshLayout.isRefreshing = it
        }
        viewModel.machineList.observeNonNull {
            (binding.recUser.adapter as MachineListAdapter).replaceData(it)
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.fetchMachines()
        }
        binding.recUser.run {
            layoutManager = GridLayoutManager(context, 2)
            adapter = MachineListAdapter {
                //onCellClick

            }
        }

        //输入框变化
        binding.etSearch.addTextChangedListener {
            //变为空 重新拉取所有机器
            //判断输入框中的文字是否为空（searchKey在布局文件中使用dataBinding进行双向绑定（@={vm.searchKey}）
            // 所以一旦输入框文字变化 这里的searchKey 也变化）
            if (viewModel.searchKey.value?.isEmpty() == true)
                viewModel.fetchMachines()
            //有变化且不为空 筛选出以文字开头的账号的用户
            else filterAsUserName()
        }

        //输入框键盘事件监听（仅在需要输入框中的回车位置的按键事件时使用此方法）
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //键盘的搜索按钮点击事件
                filterAsUserName()
            }
            false
        }

    }

    //拉取所有用户的个人信息
    private fun filterAsUserName() {
        viewModel.machineList.postValue(viewModel.oldMachineList.filter {
            it.machineMacAddress.startsWith(
                    viewModel.searchKey.value ?: ""
            ) ?:false
        })
    }

    //初始化数据
    override fun initData() {
        viewModel.fetchMachines()
    }




}



