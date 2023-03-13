package com.example.demomeow.data.remote

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

/**
 * Factory object to build Rest APIs and okhttp clients to use in remove module
 */
object ServiceGenerator {
    private const val CONNECT_TIME_OUT = 60000L
    private const val READ_TIME_OUT = 20000L
    private const val WRITE_TIME_OUT = 30000L

    /**
     * Builds given [T] retrofit restApi interface
     *
     * @param baseUrl [String] with a valid baseUrl including http scheme
     * @param gson
     * @param interceptors [List] of [Interceptor] to attach to the expected client
     */

    internal fun <T> buildRestApi(
        baseUrl: String,
        restApi: Class<T>,
        gson: Gson,
        interceptors: List<Interceptor>
    ): T {
        val okHttpClient = buildOkHttpClient(interceptors)
        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
        return builder.create(restApi)
    }

    /**
     * Builds an [OkHttpClient] with the given interceptors attached to it
     *
     * @param interceptors [List] of [Interceptor] to attach to the expected client
     */
    internal fun buildOkHttpClient(interceptors: List<Interceptor>): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                setLevel(HttpLoggingInterceptor.Level.NONE)
            }
        }
        return OkHttpClient.Builder().apply {
            for (interceptor in interceptors) addInterceptor(interceptor)
            addInterceptor(logInterceptor)
            connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
            readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
            readTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
        }.build()
    }

}
