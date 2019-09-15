package io.ashdavies.playground.repos

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.ashdavies.playground.models.Repo

internal class RepoViewState(
    val data: LiveData<PagedList<Repo>>,
    val errors: LiveData<Throwable>
)
