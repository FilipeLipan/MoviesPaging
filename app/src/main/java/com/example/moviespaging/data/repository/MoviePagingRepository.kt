package com.example.moviespaging.data.repository

import androidx.paging.PagedList
import com.example.moviespaging.data.api.response.Movie
import com.example.moviespaging.data.base.Listing
import com.example.moviespaging.data.paging.BasePagingRepository
import kotlinx.coroutines.CoroutineScope
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class MoviePagingRepository(private val scope: CoroutineScope) :
    BasePagingRepository(scope), IMoviePagingRepository<Movie, MovieRepository.Params>, KoinComponent {

    private val movieRepository: MovieRepository by inject()

    override val config = PagedList.Config.Builder()
        .setPageSize(20)
        .setInitialLoadSizeHint(20 * 2)
        .setEnablePlaceholders(false)
        .build()

    override fun execute(params: MovieRepository.Params): Listing<Movie> {
        return listingFactory({ page, movieRepositoryParams -> movieRepository.getMoviesList(page,movieRepositoryParams)}, params)
    }
}