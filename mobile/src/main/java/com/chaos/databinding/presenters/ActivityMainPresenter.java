package com.chaos.databinding.presenters;

import com.chaos.databinding.models.User;
import com.chaos.databinding.views.UserView;

public class ActivityMainPresenter {
    private final UserView view;

    public ActivityMainPresenter(final UserView view) {
        this.view = view;
    }

    public static ActivityMainPresenter create(final UserView view) {
        return new ActivityMainPresenter(view);
    }

    public void updateUser() {
        this.view.setUser(this.getUser());
    }

    private User getUser() {
        return new User("Ash Davies", "Berlin");
    }
}
