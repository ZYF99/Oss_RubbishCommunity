package com.zzz.oss_rubbishcommunity.ui.fragment.news

import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter
import com.zzz.oss_rubbishcommunity.R
import com.zzz.oss_rubbishcommunity.databinding.CellNewsBinding
import com.zzz.oss_rubbishcommunity.model.api.news.NewsResultModel

class NewsListAdapter(
        onCellClick: (NewsResultModel.News) -> Unit,
        val onCellLongClick: (NewsResultModel.News) -> Unit
) : BaseRecyclerAdapter<NewsResultModel.News, CellNewsBinding>(
    R.layout.cell_news,
    onCellClick
) {
    override fun bindData(binding: CellNewsBinding, position: Int) {
        binding.news = baseList[position]
        binding.cell.setOnLongClickListener {
            onCellLongClick(baseList[position])
            true
        }
    }
}