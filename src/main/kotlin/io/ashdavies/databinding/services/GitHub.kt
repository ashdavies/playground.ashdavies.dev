package io.ashdavies.databinding.services

import io.ashdavies.databinding.models.Repo
import io.ashdavies.databinding.models.User
import retrofit2.http.GET
import retrofit2.http.Path

internal interface GitHub {

  @GET("/users/{user}")
  suspend fun user(@Path("user") user: CharSequence): User

  @GET("/users/{user}/repos")
  suspend fun repos(@Path("user") user: CharSequence): List<Repo>
}
