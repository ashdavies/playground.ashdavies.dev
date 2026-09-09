package dev.ashdavies.playground.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.cash.sqldelight.Query
import app.cash.sqldelight.SuspendingTransacter
import app.cash.sqldelight.async.coroutines.awaitAsList
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

internal class KeyedQueryPagingSource<Key : Any, RowType : Any>(
    private val queryProvider: (beginInclusive: Key, endExclusive: Key?) -> Query<RowType>,
    private val pageBoundariesProvider: (anchor: Key?, limit: Long) -> Query<Key>,
    private val transacter: SuspendingTransacter,
    private val context: CoroutineContext,
) : PagingSource<Key, RowType>(), Query.Listener {

    private var currentQuery: Query<RowType>? by Delegates.observable(null) { _, old, new ->
        old?.removeListener(this)
        new?.addListener(this)
    }

    private var pageBoundaries: List<Key>? = null

    override val jumpingSupported: Boolean get() = false

    init {
        registerInvalidatedCallback {
            currentQuery?.removeListener(this)
            currentQuery = null
        }
    }

    override fun getRefreshKey(state: PagingState<Key, RowType>): Key? {
        val boundaries = pageBoundaries ?: return null
        val last = state.pages.lastOrNull() ?: return null
        val keyIndexFromNext = last.nextKey?.let { boundaries.indexOf(it) - 1 }
        val keyIndexFromPrev = last.prevKey?.let { boundaries.indexOf(it) + 1 }
        val keyIndex = keyIndexFromNext ?: keyIndexFromPrev ?: return null

        return boundaries.getOrNull(keyIndex)
    }

    override suspend fun load(params: LoadParams<Key>): LoadResult<Key, RowType> {
        return withContext(context) {
            try {
                transacter.transactionWithResult {
                    val boundaries = pageBoundaries
                        ?: pageBoundariesProvider(params.key, params.loadSize.toLong())
                            .awaitAsList()
                            .also { pageBoundaries = it }

                    if (boundaries.isEmpty()) {
                        @Suppress("UNCHECKED_CAST")
                        currentQuery = pageBoundariesProvider(params.key, params.loadSize.toLong()) as Query<RowType>

                        LoadResult.Page(
                            data = emptyList(),
                            prevKey = null,
                            nextKey = null,
                        )
                    } else {
                        val key = params.key ?: boundaries.first()

                        require(key in boundaries)

                        val keyIndex = boundaries.indexOf(key)
                        val previousKey = boundaries.getOrNull(keyIndex - 1)
                        val nextKey = boundaries.getOrNull(keyIndex + 1)
                        val results = queryProvider(key, nextKey)
                            .also { currentQuery = it }
                            .awaitAsList()

                        LoadResult.Page(
                            data = results,
                            prevKey = previousKey,
                            nextKey = nextKey,
                        )
                    }
                }
            } catch (e: Exception) {
                if (e is IllegalArgumentException) throw e
                LoadResult.Error(e)
            }
        }
    }

    override fun queryResultsChanged() = invalidate()
}
