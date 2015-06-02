package com.chaos.databinding.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layoutId = onCreateViewId();
        this.setContentView(layoutId);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //ButterKnife.inject(this);
    }

    protected abstract int onCreateViewId();
}
