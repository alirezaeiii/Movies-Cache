package com.sample.movies.utils

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("loadImage")
    fun bindLoadImage(view: AppCompatImageView, url: String?) {
        if (url != null) {
            Glide.with(view.context).load(url)
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("refreshing")
    fun <T> bindSwipeRefreshLayout(swipeRefreshLayout: SwipeRefreshLayout, viewState: Resource<T>?) {
        swipeRefreshLayout.isRefreshing = viewState is Resource.Loading
    }
}