package com.example.moviespaging

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.moviespaging.data.api.response.Movie
import com.example.moviespaging.data.repository.IMoviePagingRepository
import com.example.moviespaging.data.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import kotlin.coroutines.CoroutineContext

class MainViewModel() : ViewModel(), CoroutineScope, KoinComponent {
    val movieRepository: IMoviePagingRepository<Movie, MovieRepository.Params> by inject{parametersOf(this)}

    private val minReleaseDate = MutableLiveData<String>()

    private val repoResult = Transformations.map(minReleaseDate) {
        movieRepository.execute(MovieRepository.Params(it))
    }

    val movies = Transformations.switchMap(repoResult, { it.pagedList })!!
    val networkState = Transformations.switchMap(repoResult, { it.networkState })!!
    val refreshState = Transformations.switchMap(repoResult, { it.refreshState })!!
    val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job

    fun refresh() {
        val listing = repoResult?.value
        listing?.refresh ?.invoke()
    }

    fun showMovies(minRelease: String): Boolean {
        if (minReleaseDate.value == minRelease) {
            return false
        }
        minReleaseDate.value = minRelease
        return true
    }

    fun retry() {
        val listing = repoResult?.value
        listing?.retry?.invoke()
    }

    fun currentReleaseDate(): String? = minReleaseDate.value

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}