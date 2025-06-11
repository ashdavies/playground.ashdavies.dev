package io.ashdavies.paging

import kotlinx.coroutines.flow.Flow

public expect class Pager<Key : Any, Value : Any> {
    public val flow: Flow<PagingData<Value>>
}