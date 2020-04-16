package com.zzz.oss_rubbishcommunity.ui.fragment.newsdetail

import android.app.Activity
import android.content.Intent
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import com.zzhoujay.richtext.RichText
import com.zzz.oss_rubbishcommunity.R
import com.zzz.oss_rubbishcommunity.databinding.FragmentNewsDetailBinding
import com.zzz.oss_rubbishcommunity.manager.base.globalMoshi
import com.zzz.oss_rubbishcommunity.model.api.news.NewsResultModel
import com.zzz.oss_rubbishcommunity.ui.base.BaseFragment
import com.zzz.oss_rubbishcommunity.util.showAlbum
import com.zzz.oss_rubbishcommunity.util.showGallery
import java.net.URLEncoder

const val INTENT_KEY_NEWS = "news"

class NewsDetailFragment : BaseFragment<FragmentNewsDetailBinding, NewsDetailViewModel>(
        NewsDetailViewModel::class.java, R.layout.fragment_news_detail
) {

    override fun initView() {
        viewModel.news.value = globalMoshi.adapter(NewsResultModel.News::class.java)
                .fromJson(arguments?.getString(INTENT_KEY_NEWS) ?: "")
        viewModel.isAdd.value = viewModel.news.value == null
        viewModel.payloadType.value = viewModel.news.value?.payloadType

        binding.rbHtml.isEnabled = viewModel.isAdd.value ?: true
        binding.rbMd.isEnabled = viewModel.isAdd.value ?: true
        binding.rbText.isEnabled = viewModel.isAdd.value ?: true
        binding.rbUrl.isEnabled = viewModel.isAdd.value ?: true

        viewModel.isEdit.observeNonNull {
            binding.cbBanner.isEnabled = it || viewModel.isAdd.value == true
            binding.showAdd = viewModel.isEdit.value == true || viewModel.isAdd.value == true
        }

        viewModel.news.observe {
            binding.showAdd = it?.frontCoverImages?.isEmpty() ?: true
                    && (viewModel.isEdit.value == true || viewModel.isAdd.value == true)
            binding.banner.setPages(it?.frontCoverImages ?: emptyList()) {
                NewsImageBannerViewHolder(it?.frontCoverImages ?: emptyList())
            }
            loadDetail()
        }

        viewModel.news.value.run {
            if (this == null) {
                binding.rbText.isChecked = true
            } else {
                when {
                    isHTML() -> binding.rbHtml.isChecked = true
                    isURL() -> binding.rbUrl.isChecked = true
                    isMD() -> binding.rbMd.isChecked = true
                    isTEXT() -> binding.rbText.isChecked = true
                }
            }
        }

        binding.rgPayload.isEnabled = viewModel.isAdd.value ?: false

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
            setBannerPageClickListener { _, _ ->
                if (viewModel.isEdit.value == true || viewModel.isAdd.value == true) {
                    showAlbum(9)
                }
            }
        }

        binding.rgPayload.setOnCheckedChangeListener { _, checkedId ->
            viewModel.payloadType.value = when (checkedId) {
                R.id.rb_text -> 1
                R.id.rb_url -> 2
                R.id.rb_md -> 3
                R.id.rb_html -> 4
                else -> 4
            }
        }

        binding.tvAdd.setOnClickListener { showAlbum(9) }

        viewModel.isEdit.observeNonNull {
            if (it) binding.tvTitle.requestFocus()
        }

        //开启编辑
        binding.btnEdit.setOnClickListener {
            viewModel.isEdit.value = !(viewModel.isEdit.value ?: false)
        }

        //删除
        binding.btnDelete.setOnClickListener {
            showDeleteDialog {
                viewModel.deleteNews {
                    activity?.finish()
                }
            }
        }

        //完成编辑
        binding.btnFinishEdit.setOnClickListener {
            viewModel.isEdit.value = false
            val news = viewModel.news.value
            viewModel.news.value = news?.copy(
                    title = binding.tvTitle.text.toString(),
                    newsType = if (binding.cbBanner.isChecked) 1 else 0,
                    payload = binding.tvRichEdit.text.toString()
            )
            viewModel.hasEditImage.postValue(false)
            viewModel.editNews()
        }

        //发布
        binding.btnPublish.setOnClickListener {
            viewModel.addNews(
                    title = binding.tvTitle.text.toString(),
                    newsType = if (binding.cbBanner.isChecked) 1 else 0,
                    payload = binding.tvRichEdit.text.toString(),
                    payloadType = viewModel.payloadType.value?:1
            ) {
                viewModel.hasEditImage.postValue(false)
                activity?.finish()
            }
        }

        binding.btnBack.setOnClickListener { activity?.finish() }
    }

    override fun initData() {


    }

    private fun loadDetail() {
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

    //选图后的回调
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val images: List<LocalMedia>
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调
                images = PictureSelector.obtainMultipleResult(data)
                if(images.isNotEmpty())viewModel.hasEditImage.postValue(true)
                val imgs = images.map { it.path }
                //重置banner里的图片列表
                viewModel.news.value = (viewModel.news.value
                        ?: NewsResultModel.News.createDefault()).copy(frontCoverImages = imgs)
            }
        }
    }

}