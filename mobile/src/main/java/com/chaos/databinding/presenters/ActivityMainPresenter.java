package com.chaos.databinding.presenters;

import com.chaos.databinding.models.User;
import com.chaos.databinding.services.GitHub;
import com.chaos.databinding.views.UserView;

import retrofit.RestAdapter;
import rx.Observer;

public class ActivityMainPresenter implements Observer<User> {
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
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(GITHUB_API)
                .build();
    }

    public void getUser(final String user) {
        this.service.user(user).subscribe(this);
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(final Throwable exception) {
        this.view.toast(exception.getMessage());
    }

    @Override
    public void onNext(final User user) {
        this.view.setUser(user);
    }
}
