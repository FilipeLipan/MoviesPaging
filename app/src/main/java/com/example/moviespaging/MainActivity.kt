package com.example.moviespaging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView
import com.example.moviespaging.R
import com.example.moviespaging.data.base.NetworkState
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    val mainViewModel: MainViewModel by viewModel()
    lateinit var pagingAdapter :PagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iniAdapter()
        observeViewModel()
        mainViewModel.showMovies("")
    }

    fun iniAdapter(){
        pagingAdapter = PagingAdapter()
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = pagingAdapter
        pagingAdapter.setEnableLoadMore(true)
        pagingAdapter.setLoadMoreView(SimpleLoadMoreView())

        swipe_refresh.setOnRefreshListener {
            pagingAdapter.setEnableLoadMore(false)
            mainViewModel.refresh()
            pagingAdapter.setOnLoadMoreListener(null)
        }
    }

    fun observeViewModel(){
        mainViewModel.movies.observe(this, Observer {
            pagingAdapter.setEnableLoadMore(true)
            pagingAdapter.submitList(it)
        })

        mainViewModel.networkState.observe(this, Observer {
            pagingAdapter.setNetworkState(it)
            pagingAdapter.setOnLoadMoreListener{
                mainViewModel.retry()
            }
        })

        mainViewModel.refreshState.observe(this, Observer {
            swipe_refresh.isRefreshing = it == NetworkState.LOADING
        })
    }
}
