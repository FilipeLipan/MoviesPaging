package com.example.moviespaging.data.repository

import com.example.moviespaging.data.api.RestApi
import com.example.moviespaging.data.api.response.Movie
import com.example.moviespaging.data.api.response.PagedResponse
import io.coroutines.cache.core.CachePolicy
import io.coroutines.cache.core.CoroutinesCache
import java.util.concurrent.TimeUnit

class MovieRepository (private val restApi: RestApi, val coroutinesCache: CoroutinesCache, val language :String){

    suspend fun getMoviesList(page: String, params: Params) : PagedResponse<Movie> {

        val pagedMoviesResponse: PagedResponse<Movie> = coroutinesCache
                .asyncCache({
                    restApi.getMoviesList(page= page, minReleaseDate = params.minReleaseDate)
                },
                page + language,
                CachePolicy.LifeCache(15, TimeUnit.MINUTES)).await()

        val genresMap: Map<Int, String> = coroutinesCache
            .asyncCache({
                restApi.getGenres()
            },"categories" + language,
                CachePolicy.LifeCache(15, TimeUnit.MINUTES)).await().toMap()

        pagedMoviesResponse.results.map { movies ->
            movies.genres = movies.genreIds.joinToString {genreId ->
                "${genresMap.get(genreId)}"
            }
        }

        return pagedMoviesResponse;
    }

    data class Params(val minReleaseDate: String = "")
}