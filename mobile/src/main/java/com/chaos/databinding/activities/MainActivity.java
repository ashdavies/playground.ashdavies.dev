package com.chaos.databinding.activities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chaos.databinding.R;
import com.chaos.databinding.models.User;
import com.chaos.databinding.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        final User user = this.getUser();

        binding.setUser(user);
    }

    private User getUser() {
        return new User("Ash Davies", "Berlin");
    }
}
