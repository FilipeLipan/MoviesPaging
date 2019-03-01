package com.example.moviespaging.data.api

import com.example.moviespaging.BuildConfig
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier


class RestClient(val language: String) {

    val api: RestApi

    private var httpClient: OkHttpClient

    init {

        val httpClientBuilder = OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .hostnameVerifier(getHostnameVerifier())
                .addInterceptor(this::accessTokenInterceptor)

//        if (BuildConfig.HTTP_LOG_ENABLED) {
            httpClientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
//        }

        httpClient = httpClientBuilder.build()

        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create()

        val retrofitClient: Retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.HOST)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()

        api = retrofitClient.create(RestApi::class.java)
    }

    @Throws(IOException::class)
    private fun accessTokenInterceptor(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val requestBuilder = request.newBuilder()
                .header(CONTENT_TYPE, CONTENT_TYPE_JSON)
                .header(ACCEPT, CONTENT_TYPE_JSON)

        //TODO hide api-key
        val url = requestBuilder.build().url().newBuilder()
            .addQueryParameter("api_key", BuildConfig.MOVIES_DB_API_KEY)
            .addQueryParameter("language", language)
            .build()
        request = request.newBuilder().url(url).build()

        return chain.proceed(request)
    }

    private fun getHostnameVerifier(): HostnameVerifier {
        return HostnameVerifier { _, _ ->
            true
        }
    }

    companion object {
        const val READ_TIMEOUT = 120L
        const val CONNECT_TIMEOUT = 120L
        private const val TOKEN = "x-access-token"
        private const val ACCEPT = "Accept"
        private const val CONTENT_TYPE = "Content-Type"
        private const val CONTENT_TYPE_JSON = "application/json"
    }
}
