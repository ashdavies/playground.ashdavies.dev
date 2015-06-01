package com.chaos.databinding.activities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chaos.databinding.R;
import com.chaos.databinding.models.User;
import com.chaos.databinding.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.updateUser();
    }

    private void updateUser() {
        binding.setUser(this.getUser());
    }

    private User getUser() {
        return new User("Ash Davies", "Berlin");
    }
}
