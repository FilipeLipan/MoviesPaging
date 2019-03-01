package com.example.moviespaging.data.repository

import com.example.moviespaging.data.base.Listing

interface IMoviePagingRepository<ReturnType, ParamsType> {

    fun execute(params: ParamsType) : Listing<ReturnType>;
}