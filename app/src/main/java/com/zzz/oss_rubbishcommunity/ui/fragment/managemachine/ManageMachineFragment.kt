package com.zzz.oss_rubbishcommunity.ui.fragment.managemachine

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
            (binding.recMachine.adapter as MachineListAdapter).replaceData(it)
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.fetchMachines()
        }
        binding.recMachine.run {
            layoutManager = GridLayoutManager(context, 2)
            adapter = MachineListAdapter {
                //onCellClick

            }
        }

        //输入框变化
        binding.etSearch.addTextChangedListener {
            if (viewModel.searchKey.value?.isEmpty() == true)
                viewModel.machineList.postValue(viewModel.oldMachineList)
            else filterAsMacAddress()
        }

        //增加按钮
        binding.btnAdd.setOnClickListener {
            showManageMachineDialog {
                viewModel.registerMachine(
                        machineMacAddress = it.etMacAddress.text.toString(),
                        machineName = it.etName.text.toString(),
                        machineType = it.etType.text.toString().toInt(),
                        machineVersion = it.etVersion.text.toString()
                )
            }
        }

    }

    //按Mac地址搜索设备
    private fun filterAsMacAddress() {
        viewModel.machineList.postValue(viewModel.oldMachineList.filter {
            it.machineMacAddress.startsWith(
                    viewModel.searchKey.value ?: ""
            )
        })
    }

    //初始化数据
    override fun initData() {
        viewModel.fetchMachines()
    }

}



