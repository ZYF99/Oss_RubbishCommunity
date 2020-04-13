package com.zzz.oss_rubbishcommunity.ui.fragment.newsdetail

import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import com.zzz.oss_rubbishcommunity.R
import com.zzz.oss_rubbishcommunity.databinding.FragmentNewsDetailBinding
import com.zzz.oss_rubbishcommunity.manager.base.globalMoshi
import com.zzz.oss_rubbishcommunity.model.api.news.NewsResultModel
import com.zzz.oss_rubbishcommunity.ui.base.BaseFragment

const val INTENT_KEY_NEWS = "news"


class NewsDetailFragment : BaseFragment<FragmentNewsDetailBinding, NewsDetailViewModel>(
        NewsDetailViewModel::class.java, R.layout.fragment_news_detail
) {
    override fun initView() {

        //设置webView的一些属性
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                    view: WebView?,
                    handler: SslErrorHandler,
                    error: SslError?
            ) {
                //当加载html时，如果是https请求，没有证书会报错，安卓默认会webView显示空白页面，所以需要忽略错误，让网页正常显示
                handler.proceed()//忽略证书的错误继续Load页面内容，不会显示空白页面
                super.onReceivedSslError(view, handler, error)
            }
        }
        binding.webView.settings.run {
            javaScriptEnabled = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        binding.btnDelete.setOnClickListener {
            showDeleteDialog {
                viewModel.deleteNews()
            }
        }
    }

    override fun initData() {
        viewModel.news.value = globalMoshi.adapter(NewsResultModel.News::class.java)
                .fromJson(activity?.intent?.getStringExtra(INTENT_KEY_NEWS) ?: "")
    }

    private fun showDeleteDialog(onDeleteClick: () -> Unit) {
        AlertDialog.Builder(context!!)
                .setTitle("警告")
                .setMessage("删除后将不能恢复，确认删除吗")
                .setCancelable(true)
                .setPositiveButton("确认") { _, _ ->
                    onDeleteClick()
                }
                .create()
                .show()
    }

}