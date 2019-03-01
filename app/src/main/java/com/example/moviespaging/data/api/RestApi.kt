package com.example.moviespaging.data.api

import com.example.moviespaging.data.api.response.Genres
import com.example.moviespaging.data.api.response.Movie
import com.example.moviespaging.data.api.response.PagedResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface RestApi {

    companion object {
        const val SORTED_BY_MOST_POPULAR = "primary_release_date.asc"
        const val SORTED_BY = "sort_by"
    }


    @GET("discover/movie")
    @Headers(SORTED_BY + ":" + SORTED_BY_MOST_POPULAR)
    fun getMoviesList(@Query("page") page: String,
                      @Query("primary_release_date.gte") minReleaseDate: String) : Deferred<PagedResponse<Movie>>

    @GET("search/movie")
    fun searchMovie(@Query("page") page: String,
                    @Query("query") query: String,
                    @Query("primary_release_date.gte") minReleaseDate: String) : Deferred<PagedResponse<Movie>>

    @GET("genre/movie/list")
    fun getGenres(@Query("language") language: String = "pt-br"): Deferred<Genres>
}


