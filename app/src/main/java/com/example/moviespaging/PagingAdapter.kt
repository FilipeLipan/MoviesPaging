package com.example.moviespaging

import com.example.moviespaging.R
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BasePagedQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.moviespaging.data.api.response.Movie
import com.example.moviespaging.data.base.NetworkState
import com.example.moviespaging.data.base.Status


class PagingAdapter() : BasePagedQuickAdapter<Movie, BaseViewHolder>(R.layout.item_movie, movieDiffCallback) {

    override fun convert(helper: BaseViewHolder, item: Movie?) {
        item?.let {
            helper.setText(R.id.movieTitle, item.originalTitle);
        }
    }

    companion object {
        val movieDiffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        if(!isLoadMoreEnable){
            return;
        }
        when(newNetworkState?.status){
            Status.RUNNING -> {
//                notifyLoadMoreToLoading()
            }
            Status.FAILED -> {
                loadMoreFail()
            }
        }
    }
}