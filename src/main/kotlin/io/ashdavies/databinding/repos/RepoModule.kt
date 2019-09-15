package io.ashdavies.databinding.repos

import android.content.Context
import io.ashdavies.databinding.database.GitHubDatabase
import io.ashdavies.databinding.extensions.database
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val GITHUB_API = "https://api.github.com"

internal val retrofit: Retrofit
  get() = Retrofit.Builder()
      .baseUrl(GITHUB_API)
      .client(client)
      .addConverterFactory(converter)
      .build()

private val client: OkHttpClient
  get() = OkHttpClient.Builder()
      .addInterceptor(logger)
      .build()

private val converter: Converter.Factory
  get() = MoshiConverterFactory.create()

private val logger: HttpLoggingInterceptor
  get() {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HEADERS
    return interceptor
  }

internal fun database(context: Context): GitHubDatabase {
  return context.database("GitHub.db")
}
