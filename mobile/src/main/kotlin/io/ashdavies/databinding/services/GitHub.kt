package io.ashdavies.databinding.services

import io.ashdavies.databinding.models.Repo
import io.ashdavies.databinding.models.User
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

internal interface GitHub {

  @GET("/users/{user}")
  fun getUser(@Path("user") user: String): Deferred<User>

  @GET("/users/{user}/repos")
  fun getRepos(@Path("user") user: String): Deferred<List<Repo>>
}
