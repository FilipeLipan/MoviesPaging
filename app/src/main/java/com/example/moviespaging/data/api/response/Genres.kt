package com.example.moviespaging.data.api.response

open class Genres {
    open var genres: ArrayList<Genre> = ArrayList()

    fun toMap() : Map<Int, String> {
        return genres.map { it.id to it.name }.toMap()
    }
}