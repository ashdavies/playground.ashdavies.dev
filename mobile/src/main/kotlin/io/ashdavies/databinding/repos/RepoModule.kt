package io.ashdavies.databinding.repos

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val GITHUB_API = "https://api.github.com"

internal val retrofit: Retrofit
  get() = Retrofit.Builder()
      .baseUrl(GITHUB_API)
      .client(client)
      .addCallAdapterFactory(CoroutineCallAdapterFactory())
      .addConverterFactory(MoshiConverterFactory.create())
      .build()

private val client: OkHttpClient
  get() = OkHttpClient.Builder()
      .addInterceptor(logger)
      .build()

private val logger: HttpLoggingInterceptor
  get() = HttpLoggingInterceptor()
      .setLevel(HttpLoggingInterceptor.Level.HEADERS)
