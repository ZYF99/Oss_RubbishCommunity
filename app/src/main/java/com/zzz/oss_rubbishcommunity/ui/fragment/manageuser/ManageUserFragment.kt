package com.zzz.oss_rubbishcommunity.ui.fragment.manageuser

import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.zzz.oss_rubbishcommunity.R
import com.zzz.oss_rubbishcommunity.databinding.FragmentManageUserBinding
import com.zzz.oss_rubbishcommunity.manager.sharedpref.SharedPrefModel
import com.zzz.oss_rubbishcommunity.ui.activity.showLoginActivity
import com.zzz.oss_rubbishcommunity.ui.base.BaseFragment

class ManageUserFragment : BaseFragment<FragmentManageUserBinding, ManageUserViewModel>(
        ManageUserViewModel::class.java, R.layout.fragment_manage_user
) {

    override fun initView() {
        viewModel.isRefreshing.observeNonNull {
            binding.refreshLayout.isRefreshing = it
        }
        viewModel.userList.observeNonNull {
            (binding.recUser.adapter as UserListAdapter).replaceData(it)
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.fetchUsers()
        }
        binding.recUser.run {
            layoutManager = GridLayoutManager(context, 2)
            adapter = UserListAdapter {
                //onCellClick

            }
        }

        //注销按钮监听
        binding.btnLogout.setOnClickListener {
            SharedPrefModel.hasLogin = false
            SharedPrefModel.token = ""
            activity?.run {
                showLoginActivity(this, true)
            }
        }

        //输入框变化
        binding.etSearch.addTextChangedListener {
            if (viewModel.searchKey.value?.isEmpty() == true) viewModel.fetchUsers()
            else filterAsUserName()
        }

    }

    //拉取所有用户的个人信息
    private fun filterAsUserName() {
        viewModel.userList.postValue(viewModel.oldUserList.filter {
            it.name?.contains(
                    viewModel.searchKey.value ?: ""
            ) ?: false
        })
    }

    //初始化数据
    override fun initData() {
        viewModel.fetchUsers()
    }


}



