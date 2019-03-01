package com.example.moviespaging.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.moviespaging.data.api.response.PagedResponse
import com.example.moviespaging.data.base.NetworkState
import kotlinx.coroutines.*
import java.lang.Exception

class BaseDataSource<ReturnType, ParamsType>(
    private val getFunction: (suspend (page: String, params: ParamsType) -> PagedResponse<ReturnType>),
    private val params: ParamsType,
    private val scope: CoroutineScope) : PageKeyedDataSource<Long, ReturnType>(){

    // keep a function reference for the retry event
    var retry: (() -> Any)? = null
    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    override fun loadInitial(loadInitialParams: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, ReturnType>) {
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)
        scope.launch {
            try {
                networkState.postValue(NetworkState.LOADED)
                initialLoad.postValue(NetworkState.LOADED)
                val pagedResponse = getFunction("1", params)
                callback.onResult(pagedResponse.results, 1, (pagedResponse.page + 1).toLong())
            } catch (exception: Throwable) {
                retry = {
                    loadInitial(loadInitialParams, callback)
                }
                val error = NetworkState.error(
                    exception.message ?: "unknown error"
                )
                networkState.postValue(error)
                initialLoad.postValue(error)
            }
        }

    }

    override fun loadAfter(loadParams: LoadParams<Long>, callback: LoadCallback<Long, ReturnType>) {
        networkState.postValue(NetworkState.LOADING)

        scope.launch{

            try {
                val pagedResponse = getFunction.invoke(loadParams.key.toString(), params)
                retry = null
                networkState.postValue(NetworkState.LOADED)
                callback.onResult(pagedResponse.results, (pagedResponse.page + 1).toLong())

            } catch (exception: Exception) {
                retry = {
                    loadAfter(loadParams, callback)
                }
                networkState.postValue(
                    NetworkState.error(
                        exception.message ?: "unknown err"
                    )
                )
            }
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, ReturnType>) {
    }

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            it.invoke()
        }
    }
}