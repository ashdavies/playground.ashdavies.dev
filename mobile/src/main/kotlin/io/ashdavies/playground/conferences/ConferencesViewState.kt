package io.ashdavies.playground.conferences

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.ashdavies.playground.models.Repo

internal class ConferencesViewState(
    val data: LiveData<PagedList<Repo>>,
    val errors: LiveData<Throwable>
)
