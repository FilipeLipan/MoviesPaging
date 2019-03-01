package com.example.moviespaging.data.api.response

import com.google.gson.annotations.SerializedName

open class PagedResponse<T> {
    @SerializedName("total_results")
    open var totalResults: Int = 0
    @SerializedName("total_pages")
    open var totalPages: Int = 0
    open var page: Int = 1
    open var results: ArrayList<T> = ArrayList()
}