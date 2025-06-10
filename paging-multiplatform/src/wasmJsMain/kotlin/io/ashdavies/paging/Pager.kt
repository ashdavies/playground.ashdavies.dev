package io.ashdavies.paging

public actual class Pager<Key : Any, Value : Any> {
    actual val flow: Flow<PagingData<Value>>
        get() = TODO("Not yet implemented")
}