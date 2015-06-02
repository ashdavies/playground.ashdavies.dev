package com.chaos.databinding.activities;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int layoutId = this.onCreateViewId();
        this.setContentView(layoutId);

        final int toolbarId = this.onCreateViewToolbarId();
        this.setSupportActionBar((Toolbar)this.findViewById(toolbarId));
    }

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        //ButterKnife.inject(this);
    }

    @LayoutRes
    protected abstract int onCreateViewId();

    @IdRes
    protected abstract int onCreateViewToolbarId();
}
