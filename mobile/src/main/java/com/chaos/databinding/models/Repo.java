package com.chaos.databinding.models;


import com.google.gson.annotations.SerializedName;

public class Repo {
    private final String name;
    private final String description;
    private final String language;

    @SerializedName("updated_at")
    private final String updatedAt;

    @SerializedName("stargazers_count")
    private final String stargazersCount;

    @SerializedName("watchers_count")
    private final String watchersCount;

    public Repo(final String name, final String description, final String language,
                final String updatedAt, final String stargazersCount, final String watchersCount) {
        this.name = name;
        this.description = description;
        this.language = language;
        this.updatedAt = updatedAt;
        this.stargazersCount = stargazersCount;
        this.watchersCount = watchersCount;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public String getStargazersCount() {
        return this.stargazersCount;
    }

    public String getWatchersCount() {
        return this.watchersCount;
    }
}
