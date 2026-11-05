package dev.ashdavies.playground.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.cash.sqldelight.Query
import app.cash.sqldelight.SuspendingTransacter
import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOne
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

internal class AsyncOffsetQueryPagingSource<RowType : Any> private constructor(
    private val queryProvider: (limit: Int, offset: Int) -> Query<RowType>,
    private val countQuery: Query<Int>,
    private val transacter: SuspendingTransacter,
    private val context: CoroutineContext,
    private val placeholderCount: Int,
    private val initialOffset: Int,
) : PagingSource<Int, RowType>(), Query.Listener {

    private var currentQuery: Query<RowType>? by Delegates.observable(null) { _, old, new ->
        old?.removeListener(this)
        new?.addListener(this)
    }

    override val jumpingSupported get() = true

    init {
        registerInvalidatedCallback {
            currentQuery?.removeListener(this)
            currentQuery = null
        }
    }

    constructor(
        countQuery: Query<Long>,
        transacter: SuspendingTransacter,
        context: CoroutineContext,
        queryProvider: (limit: Long, offset: Long) -> Query<RowType>,
        placeholderCount: Int,
        initialOffset: Long = 0,
    ) : this(
        queryProvider = { limit, offset -> queryProvider(limit.toLong(), offset.toLong()) },
        countQuery = countQuery.toInt(),
        transacter = transacter,
        context = context,
        placeholderCount = placeholderCount,
        initialOffset = initialOffset.toInt(),
    )

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RowType> = withContext(context) {
        val key = params.key ?: initialOffset
        val limit = when (params) {
            is LoadParams.Prepend -> minOf(key, params.loadSize)
            else -> params.loadSize
        }

        if (invalid) {
            LoadResult.Invalid()
        } else {
            transacter.transactionWithResult {
                val count = countQuery.awaitAsOne()
                val offset = when (params) {
                    is LoadParams.Prepend -> maxOf(0, key - params.loadSize)

                    is LoadParams.Append -> key

                    is LoadParams.Refresh -> if (key >= count - params.loadSize) {
                        maxOf(0, count - params.loadSize)
                    } else {
                        key
                    }
                }
                val data = queryProvider(limit, offset)
                    .also { currentQuery = it }
                    .awaitAsList()
                val nextPosToLoad = offset + data.size

                LoadResult.Page(
                    data = data,
                    prevKey = offset.takeIf { it > 0 && data.isNotEmpty() },
                    nextKey = nextPosToLoad.takeIf { data.isNotEmpty() && data.size >= limit && it < count },
                    itemsBefore = offset,
                    itemsAfter = if (count > 0) maxOf(0, count - nextPosToLoad) else placeholderCount,
                )
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RowType>) = state.anchorPosition?.let {
        maxOf(0, it - (state.config.initialLoadSize / 2))
    }

    override fun queryResultsChanged() = invalidate()
}

private fun Query<Long>.toInt() = object : Query<Int>({ cursor -> mapper(cursor).toInt() }) {
    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>) = this@toInt.execute(mapper)
    override fun addListener(listener: Listener) = this@toInt.addListener(listener)
    override fun removeListener(listener: Listener) = this@toInt.removeListener(listener)
}
