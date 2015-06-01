package com.chaos.databinding.models;

public class User {
    private final String name;
    private final String location;

    public User(final String name, final String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return this.name;
    }

    public String getLocation() {
        return this.location;
    }
}