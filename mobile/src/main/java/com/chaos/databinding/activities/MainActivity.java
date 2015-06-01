package com.chaos.databinding.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.chaos.databinding.R;
import com.chaos.databinding.adapters.RepoAdapter;
import com.chaos.databinding.models.Repo;
import com.chaos.databinding.presenters.ActivityMainPresenter;
import com.chaos.databinding.views.ListingsView;

public class MainActivity extends AppCompatActivity implements ListingsView<Repo> {
    private static final String DEFAULT_USER = "ashdavies";

    private ActivityMainPresenter presenter;

    private RecyclerView repos;
    private RepoAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        this.presenter = ActivityMainPresenter.create(this);

        this.repos = (RecyclerView) findViewById(R.id.repos);
        repos.setLayoutManager(new LinearLayoutManager(this));

        this.adapter = new RepoAdapter();
        this.repos.setAdapter(this.adapter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        this.presenter.getRepos(DEFAULT_USER);
    }

    @Override
    public void toast(final String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addItem(final Repo repo) {
        this.adapter.addItem(repo);
    }

    @Override
    public void addItems(final Repo[] repos) {
        this.adapter.addItems(repos);
    }

    @Override
    public void clearItems() {
        this.adapter.clearItems();
    }
}
