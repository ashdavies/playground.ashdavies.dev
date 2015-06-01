package com.chaos.databinding.presenters;

import com.chaos.databinding.models.Repo;
import com.chaos.databinding.services.GitHub;
import com.chaos.databinding.views.ListingsView;

import retrofit.RestAdapter;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ActivityMainPresenter implements Observer<Repo[]> {
    private static final String GITHUB_API = "https://api.github.com";

    private final GitHub service;
    private final ListingsView<Repo> view;

    public ActivityMainPresenter(final GitHub service, final ListingsView<Repo> view) {
        this.service = service;
        this.view = view;
    }

    public static ActivityMainPresenter create(final ListingsView<Repo> view) {
        final GitHub service = ActivityMainPresenter.getRestAdapter().create(GitHub.class);
        return new ActivityMainPresenter(service, view);
    }

    private static RestAdapter getRestAdapter() {
        return new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setEndpoint(GITHUB_API)
                .build();
    }

    public void getRepos(final String user) {
        this.service.getRepos(user)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(final Throwable exception) {
        this.view.toast(exception.getMessage());
    }

    @Override
    public void onNext(final Repo[] repos) {
        this.view.clearItems();
        this.view.addItems(repos);
    }
}
