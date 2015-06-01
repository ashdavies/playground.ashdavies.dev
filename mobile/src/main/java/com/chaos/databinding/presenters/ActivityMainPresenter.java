package com.chaos.databinding.presenters;

import com.chaos.databinding.models.User;
import com.chaos.databinding.services.GitHub;
import com.chaos.databinding.views.UserView;

import retrofit.RestAdapter;

public class ActivityMainPresenter {
    private static final String GITHUB_API = "https://api.github.com";

    private final GitHub service;
    private final UserView view;

    public ActivityMainPresenter(final GitHub service, final UserView view) {
        this.service = service;
        this.view = view;
    }

    public static ActivityMainPresenter create(final UserView view) {
        final GitHub service = ActivityMainPresenter.getRestAdapter().create(GitHub.class);
        return new ActivityMainPresenter(service, view);
    }

    private static RestAdapter getRestAdapter() {
        return new RestAdapter.Builder()
                .setEndpoint(GITHUB_API)
                .build();
    }

    public void updateUser() {
        this.view.setUser(this.getUser());
    }

    private User getUser() {
        return new User("Ash Davies", "Berlin");
    }
}
