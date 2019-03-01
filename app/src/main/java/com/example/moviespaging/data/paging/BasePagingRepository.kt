package com.example.moviespaging.data.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moviespaging.data.api.response.PagedResponse
import com.example.moviespaging.data.base.Listing
import com.example.moviespaging.data.repository.IMoviePagingRepository
import kotlinx.coroutines.CoroutineScope

abstract class BasePagingRepository(private val scope: CoroutineScope) {

    abstract val config :PagedList.Config

    fun<ListingType, ParamsType> listingFactory(getFunction: (suspend (page: String, params: ParamsType) -> PagedResponse<ListingType>), params: ParamsType): Listing<ListingType> {
        val sourceFactory = BaseDataFactory<ListingType, ParamsType> (getFunction, params, scope)
        val livePagedList : LiveData<PagedList<ListingType>> = LivePagedListBuilder<Long, ListingType>(sourceFactory, config).build()

        return Listing(
            pagedList = livePagedList,
            networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.networkState
            },
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            },
            refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.initialLoad
            })
    }
}
