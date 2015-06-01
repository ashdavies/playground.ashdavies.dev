package com.chaos.databinding.models;

import com.google.gson.annotations.SerializedName;

public class Repo {
    private final String name;

    @SerializedName("full_name")
    private final String fullName;

    public Repo(final String name, final String fullName) {
        this.name = name;
        this.fullName = fullName;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }
}
