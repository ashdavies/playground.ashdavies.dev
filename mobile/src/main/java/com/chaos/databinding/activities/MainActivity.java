package com.chaos.databinding.activities;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.chaos.databinding.R;
import com.chaos.databinding.adapters.RepoAdapter;
import com.chaos.databinding.models.Repo;
import com.chaos.databinding.presenters.ActivityMainPresenter;
import com.chaos.databinding.views.ListingsView;

public class MainActivity extends BaseActivity implements ListingsView<Repo>, SearchView.OnQueryTextListener {
    private ActivityMainPresenter presenter;
    private RepoAdapter adapter;
    private SearchView search;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.presenter = ActivityMainPresenter.create(this);
        this.adapter = new RepoAdapter(this.findViewById(R.id.empty));

        final RecyclerView recycler = (RecyclerView) this.findViewById(R.id.repos);
        recycler.setAdapter(this.adapter);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected int onCreateViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected int onCreateViewToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        this.search = (SearchView) MenuItemCompat.getActionView(item);
        this.search.setOnQueryTextListener(this);
        this.search.onActionViewExpanded();

        return true;
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

    @Override
    public boolean onQueryTextSubmit(final String query) {
        this.presenter.getRepos(query);
        this.search.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(final String query) {
        return true;
    }
}
