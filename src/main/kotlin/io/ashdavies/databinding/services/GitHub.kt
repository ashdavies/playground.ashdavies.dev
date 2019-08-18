package io.ashdavies.databinding.services

import io.ashdavies.databinding.models.Repo
import io.ashdavies.databinding.models.User
import retrofit2.http.GET
import retrofit2.http.Path

internal interface GitHub {

  @GET("/users/{user}")
  fun getUser(@Path("user") user: CharSequence): User

  @GET("/users/{user}/repos")
  fun getRepos(@Path("user") user: CharSequence): List<Repo>
}
