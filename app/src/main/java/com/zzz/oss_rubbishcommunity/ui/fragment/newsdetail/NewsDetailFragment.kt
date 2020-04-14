package com.zzz.oss_rubbishcommunity.ui.fragment.newsdetail

import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import com.zzhoujay.richtext.RichText
import com.zzz.oss_rubbishcommunity.R
import com.zzz.oss_rubbishcommunity.databinding.FragmentNewsDetailBinding
import com.zzz.oss_rubbishcommunity.manager.base.globalMoshi
import com.zzz.oss_rubbishcommunity.model.api.news.NewsResultModel
import com.zzz.oss_rubbishcommunity.ui.base.BaseFragment
import com.zzz.oss_rubbishcommunity.util.showGallery
import java.net.URLEncoder

const val INTENT_KEY_NEWS = "news"

class NewsDetailFragment : BaseFragment<FragmentNewsDetailBinding, NewsDetailViewModel>(
        NewsDetailViewModel::class.java, R.layout.fragment_news_detail
) {

    override fun initView() {
        viewModel.news.value = globalMoshi.adapter(NewsResultModel.News::class.java)
                .fromJson(activity?.intent?.getStringExtra(INTENT_KEY_NEWS) ?: "")

        viewModel.news.observeNonNull {
            loadDetail()
        }

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

        binding.banner.run {
            val imgs = viewModel.news.value?.frontCoverImages ?: emptyList()
            setPages(imgs) {
                NewsImageBannerViewHolder(imgs)
            }
        }

        viewModel.isEdit.observeNonNull {
            if (it) binding.tvTitle.requestFocus()
        }

        binding.btnEdit.setOnClickListener {
            viewModel.isEdit.value = !(viewModel.isEdit.value ?: false)
        }

        binding.btnDelete.setOnClickListener {
            showDeleteDialog {
                viewModel.deleteNews{
                    activity?.finish()
                }
            }
        }

        binding.btnFinishEdit.setOnClickListener {
            viewModel.isEdit.value = false
            val news = viewModel.news.value
            viewModel.news.value = news?.copy(
                    title = binding.tvTitle.text.toString(),
                    payload = binding.tvRichEdit.text.toString(),
                    frontCoverImages = emptyList()
            )
            viewModel.editNews()
        }

        binding.btnBack.setOnClickListener { activity?.finish() }
    }

    override fun initData() {


    }

    private fun loadDetail(){
        val news = viewModel.news.value
        when {
            news?.isMD() ?: false -> RichText.fromMarkdown(news?.payload)
                    .imageClick { imageUrls, position ->
                        showGallery(context!!, imageUrls, position)
                    }.into(binding.tvRichText)
            news?.isURL() ?: false -> binding.webView.loadUrl(news?.payload)
            news?.isHTML() ?: false || news?.isTEXT() ?: false -> binding.webView.loadData(
                    URLEncoder.encode(news?.payload, "utf-8"),
                    "text/html",
                    "utf-8"
            )
        }
    }

    private fun showDeleteDialog(onDeleteClick: () -> Unit) {
        AlertDialog.Builder(context!!)
                .setTitle("警告")
                .setMessage("删除后将不能恢复，确认删除吗")
                .setCancelable(true)
                .setNegativeButton("取消") { _, _ -> }
                .setPositiveButton("确认") { _, _ ->
                    onDeleteClick()
                }
                .create()
                .show()
    }

    override fun onDestroy() {
        RichText.clear(activity)
        super.onDestroy()
    }

}