package com.example.moviespaging.data.api.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class Movie : Serializable {
    open var id: Long = 0
    @SerializedName("original_title")
    open var originalTitle: String = ""
    @SerializedName("poster_path")
    open var posterPath: String = ""
    open var overview: String = ""
    @SerializedName("vote_average")
    open var voteAverage: Double = 0.toDouble()
    @SerializedName("release_date")
    open var releaseDate: String = ""
    @SerializedName("genre_ids")
    open var genreIds: ArrayList<Int> = ArrayList()
    var genres :String = ""
}