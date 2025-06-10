package io.ashdavies.paging

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

public actual class PagingData<T : Any> {
    public val flow: Flow<PagingData<T>> = emptyFlow()
}