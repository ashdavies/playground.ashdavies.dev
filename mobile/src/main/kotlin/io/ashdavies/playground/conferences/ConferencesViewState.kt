package io.ashdavies.playground.conferences

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.ashdavies.playground.network.Conference

internal class ConferencesViewState(
    val data: LiveData<PagedList<Conference>>,
    val errors: LiveData<Throwable>
)
