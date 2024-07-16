package io.ashdavies.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.Stable
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.slack.circuit.retained.rememberRetained
import io.ashdavies.paging.collectAsLazyPagingItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

private const val COROUTINE_SCOPE = "COROUTINE_SCOPE"

@Composable
@OptIn(ExperimentalPagingApi::class)
internal fun EventsPresenter(
    coroutineScope: CoroutineScope = rememberRetainedCoroutineScope(),
    eventPager: Pager<String, Event> = rememberEventPager(),
): EventsScreen.State {
    val pagingData = rememberRetained(coroutineScope) {
        eventPager.flow.cachedIn(coroutineScope)
    }

    return EventsScreen.State(
        pagingItems = pagingData.collectAsLazyPagingItems(),
    )
}

@Stable
private class StableCoroutineScope(scope: CoroutineScope) : CoroutineScope by scope

@Composable
private fun rememberRetainedCoroutineScope(
    context: CoroutineContext = Dispatchers.Main.immediate,
): StableCoroutineScope = rememberRetained(COROUTINE_SCOPE) {
    val coroutineScope = StableCoroutineScope(CoroutineScope(context + Job()))
    rememberObserver(coroutineScope::cancel)
    coroutineScope
}

private fun rememberObserver(onForgotten: () -> Unit) = object : RememberObserver {
    override fun onAbandoned() = onForgotten()
    override fun onForgotten() = onForgotten()
    override fun onRemembered() = Unit
}
