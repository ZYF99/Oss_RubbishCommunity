package com.zzz.oss_rubbishcommunity.ui.fragment.news

import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import com.zzz.oss_rubbishcommunity.ui.activity.ContentActivity
import com.zzz.oss_rubbishcommunity.R
import com.zzz.oss_rubbishcommunity.databinding.FragmentNewsBinding
import com.zzz.oss_rubbishcommunity.model.api.news.NewsResultModel
import com.zzz.oss_rubbishcommunity.ui.base.BaseFragment
import com.zzz.oss_rubbishcommunity.ui.fragment.newsdetail.INTENT_KEY_NEWS
import com.zzz.oss_rubbishcommunity.ui.fragment.newsdetail.NewsDetailFragment
import com.zzz.oss_rubbishcommunity.util.toJson

class NewsFragment : BaseFragment<FragmentNewsBinding, NewsViewModel>(
        NewsViewModel::class.java, R.layout.fragment_news
) {

    //初始化View的方法
    override fun initView() {

        //输入框变化
        binding.etSearch.addTextChangedListener {
            if (viewModel.searchKey.value?.isEmpty() == true)
                viewModel.newsList.postValue(viewModel.oldNesList)
            else filterAsTitle()
        }

        //输入框键盘事件监听（仅在需要输入框中的回车位置的按键事件时使用此方法）
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //键盘的搜索按钮点击事件
                filterAsTitle()
            }
            false
        }

        //刷新状态监听
        viewModel.isRefreshing.observeNonNull {
            binding.refreshLayout.isRefreshing = it //将刷新控件的状态置为当前刷新的状态it
            binding.root.isEnabled = !it //将root布局的可点击状态（实际上isEnabled是可用状态）置为当前刷新的状态it
        }

        //新闻列表数据变化监听
        viewModel.newsList.observeNonNull {
            //调用新闻列表适配器的replaceData方法替换数据
            (binding.rvNews.adapter as NewsListAdapter).replaceData(it)
        }

        fun showManageChooseDialog(news: NewsResultModel.News) {
            //弹出编辑+删除的选择弹框
            showManageChooseDialog(
                    onDeleteClick = {
                        //点击删除并确认
                        viewModel.deleteNews(newsId = news.newsId)
                    })
        }

        //配置新闻列表
        binding.rvNews.apply {
            adapter = NewsListAdapter(
                    onCellClick = { news ->
                        //新闻列表点击事件，点击跳转至详情页面
                        jumpToDetail(news)
                    },
                    onCellLongClick = { news ->
                        showManageChooseDialog(news)
                    }
            )
        }

        //添加News按钮
        binding.btnAdd.setOnClickListener {
            jumpToDetail()
        }

        //监听刷新控件
        binding.refreshLayout.setOnRefreshListener {
            viewModel.fetchNews(true)
        }
    }

    override fun initData() {
        viewModel.fetchNews(true)
    }

    //拉取所有news
    private fun filterAsTitle() {
        viewModel.newsList.postValue(viewModel.oldNesList.filter {
            it.title?.contains(
                    viewModel.searchKey.value ?: ""
            ) ?: false
        })
    }

    //跳转至新闻详情页面
    private fun jumpToDetail(news: NewsResultModel.News? = null) {
        startActivity(
                ContentActivity.createIntent(
                        context!!,
                        ContentActivity.Destination.NewsDetail,
                        bundle = bundleOf(Pair(INTENT_KEY_NEWS, news.toJson()) )
                )
        )
    }

}



