package com.example.moviespaging.di

import com.example.moviespaging.MainViewModel
import com.example.moviespaging.data.api.RestClient
import com.example.moviespaging.data.repository.IMoviePagingRepository
import com.example.moviespaging.data.repository.MoviePagingRepository
import com.example.moviespaging.data.repository.MovieRepository
import io.coroutines.cache.core.CoroutinesCache
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import com.example.moviespaging.R

object KoinModules {

    val koinModule : Module = module {
        viewModel { MainViewModel() }
        single { RestClient(androidContext().getString(R.string.language)).api }
        single { ( scope: CoroutineScope) -> MoviePagingRepository(scope) as IMoviePagingRepository<*,*> }
        single { CoroutinesCache(androidContext()) }
        single { MovieRepository (get(), get(), androidContext().getString(R.string.language)) }
    }
}