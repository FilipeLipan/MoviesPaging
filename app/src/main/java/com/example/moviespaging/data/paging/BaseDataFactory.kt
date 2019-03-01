package com.example.moviespaging.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.moviespaging.data.api.response.PagedResponse
import kotlinx.coroutines.CoroutineScope


class BaseDataFactory<ReturnType, ParamsType>(
    private val getFunction: (suspend (page: String, params: ParamsType) -> PagedResponse<ReturnType>),
    private val minRelease: ParamsType,
    private val scope: CoroutineScope) : DataSource.Factory<Long, ReturnType>() {

    val sourceLiveData = MutableLiveData<BaseDataSource<ReturnType, ParamsType>>()

    override fun create(): DataSource<Long, ReturnType> {
        val source = BaseDataSource<ReturnType, ParamsType>(getFunction, minRelease, scope)
        sourceLiveData.postValue(source)
        return source;
    }
}