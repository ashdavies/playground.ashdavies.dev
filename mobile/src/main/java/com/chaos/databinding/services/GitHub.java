package com.chaos.databinding.services;

import com.chaos.databinding.models.Repo;
import com.chaos.databinding.models.User;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface GitHub {

    @GET("/users/{user}")
    Observable<User> getUser(@Path("getUser") final String user);

    @GET("/users/{user}/repos")
    Observable<Repo[]> getRepos(@Path("user") final String user);
}
