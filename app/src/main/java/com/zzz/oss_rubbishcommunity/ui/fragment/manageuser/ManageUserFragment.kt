package com.zzz.oss_rubbishcommunity.ui.fragment.manageuser

import android.view.inputmethod.EditorInfo
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
            //变为空 重新拉取所有用户
            //判断输入框中的文字是否为空（searchKey在布局文件中使用dataBinding进行双向绑定（@={vm.searchKey}）
            // 所以一旦输入框文字变化 这里的searchKey 也变化）
            if (viewModel.searchKey.value?.isEmpty() == true)
                viewModel.fetchUsers()
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
        viewModel.userList.postValue(viewModel.oldUserList.filter {
            it.name?.startsWith(
                viewModel.searchKey.value ?: ""
            )?:false
        })
    }

    //初始化数据
    override fun initData() {
        viewModel.fetchUsers()
    }




}



