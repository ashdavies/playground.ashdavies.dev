package com.chaos.databinding.activities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chaos.databinding.R;
import com.chaos.databinding.databinding.ActivityMainBinding;
import com.chaos.databinding.models.User;
import com.chaos.databinding.presenters.ActivityMainPresenter;
import com.chaos.databinding.views.UserView;

public class MainActivity extends AppCompatActivity implements UserView {
    private ActivityMainBinding binding;
    private ActivityMainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.presenter = ActivityMainPresenter.create(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        this.presenter.updateUser();
    }

    @Override
    public void setUser(final User user) {
        this.binding.setUser(user);
    }
}
