package io.ashdavies.party.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.paging.Pager
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.presenterOf
import io.ashdavies.content.PlatformContext
import io.ashdavies.identity.IdentityManager
import io.ashdavies.party.events.EventsPresenter
import io.ashdavies.party.events.EventsScreen
import io.ashdavies.party.events.paging.rememberEventPager
import io.ashdavies.party.gallery.GalleryPresenter
import io.ashdavies.party.gallery.GalleryScreen
import io.ashdavies.party.gallery.PathProvider
import io.ashdavies.party.gallery.StorageManager
import io.ashdavies.party.home.HomePresenter
import io.ashdavies.party.home.HomeScreen
import io.ashdavies.playground.PlaygroundDatabase
import io.ashdavies.sql.LocalTransacter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext
import io.ashdavies.party.events.Event as DatabaseEvent

private const val PLAYGROUND_BASE_URL = "playground.ashdavies.dev"
private const val DEFAULT_PAGE_SIZE = 10

private const val COROUTINE_SCOPE = "COROUTINE_SCOPE"

@Composable
public fun rememberCircuit(
    platformContext: PlatformContext,
    eventPager: Pager<String, DatabaseEvent> = rememberEventPager(),
    playgroundDatabase: PlaygroundDatabase = LocalTransacter.current as PlaygroundDatabase,
    coroutineScope: CoroutineScope = rememberRetainedCoroutineScope(),
): Circuit = remember(platformContext) {
    val identityManager = IdentityManager(platformContext, playgroundDatabase.credentialQueries)
    val storageManager = StorageManager(PathProvider(platformContext))

    Circuit.Builder()
        .addPresenter<HomeScreen, HomeScreen.State> { _, navigator, _ ->
            presenterOf { HomePresenter(identityManager, coroutineScope, navigator) }
        }
        .addPresenter<EventsScreen, EventsScreen.State> { _, _, _ ->
            presenterOf { EventsPresenter(eventPager, coroutineScope) }
        }
        .addPresenter<GalleryScreen, GalleryScreen.State> { _, _, _ ->
            presenterOf { GalleryPresenter(platformContext) }
        }
        .addUi<HomeScreen, HomeScreen.State> { state, modifier ->
            HomeScreen(state, modifier)
        }
        .addUi<EventsScreen, EventsScreen.State> { state, modifier ->
            EventsScreen(state, modifier)
        }
        .addUi<GalleryScreen, GalleryScreen.State> { state, modifier ->
            GalleryScreen(state, storageManager, modifier)
        }
        .build()
}


@Stable
private class StableCoroutineScope(scope: CoroutineScope) : CoroutineScope by scope

@Composable
private fun rememberRetainedCoroutineScope(
    key: String = COROUTINE_SCOPE,
    context: CoroutineContext = Dispatchers.Main.immediate,
): StableCoroutineScope = rememberRetained(key) {
    val coroutineScope = StableCoroutineScope(CoroutineScope(context + Job()))
    rememberObserver(coroutineScope::cancel)
    coroutineScope
}

private fun rememberObserver(onForgotten: () -> Unit) = object : RememberObserver {
    override fun onAbandoned() = onForgotten()
    override fun onForgotten() = onForgotten()
    override fun onRemembered() = Unit
}
