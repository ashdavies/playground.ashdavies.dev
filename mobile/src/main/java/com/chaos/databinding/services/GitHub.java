package com.chaos.databinding.services;

import com.chaos.databinding.models.User;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface GitHub {

    @GET("/users/{user}")
    Observable<User> user(@Path("user") final String user);
}
