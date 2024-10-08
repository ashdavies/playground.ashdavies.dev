package io.ashdavies.party.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.paging.Pager
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.presenterOf
import io.ashdavies.content.PlatformContext
import io.ashdavies.content.reportFullyDrawn
import io.ashdavies.http.DefaultHttpConfiguration
import io.ashdavies.identity.IdentityManager
import io.ashdavies.party.coroutines.rememberRetainedCoroutineScope
import io.ashdavies.party.events.EventsPresenter
import io.ashdavies.party.events.EventsScreen
import io.ashdavies.party.events.paging.rememberEventPager
import io.ashdavies.party.gallery.File
import io.ashdavies.party.gallery.GalleryPresenter
import io.ashdavies.party.gallery.GalleryScreen
import io.ashdavies.party.gallery.ImageManager
import io.ashdavies.party.gallery.PathProvider
import io.ashdavies.party.gallery.StorageManager
import io.ashdavies.party.gallery.SyncManager
import io.ashdavies.party.gallery.inMemoryHttpClientEngine
import io.ashdavies.party.gallery.readChannel
import io.ashdavies.party.home.HomePresenter
import io.ashdavies.party.home.HomeScreen
import io.ashdavies.playground.PlaygroundDatabase
import io.ashdavies.sql.LocalTransacter
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import io.ashdavies.party.events.Event as DatabaseEvent

@Composable
public fun rememberCircuit(
    platformContext: PlatformContext,
    eventPager: Pager<String, DatabaseEvent> = rememberEventPager(),
    playgroundDatabase: PlaygroundDatabase = LocalTransacter.current as PlaygroundDatabase,
    coroutineScope: CoroutineScope = rememberRetainedCoroutineScope(),
): Circuit = remember(platformContext) {
    val identityManager = IdentityManager(platformContext, playgroundDatabase.credentialQueries)
    val imageManager = ImageManager(platformContext, playgroundDatabase.imageQueries)
    val inMemoryHttpClient = HttpClient(inMemoryHttpClientEngine(), DefaultHttpConfiguration)
    val syncManager = SyncManager(inMemoryHttpClient, File::readChannel)
    val storageManager = StorageManager(PathProvider(platformContext))

    Circuit.Builder()
        .addPresenter<HomeScreen, HomeScreen.State> { _, navigator, _ ->
            presenterOf { HomePresenter(identityManager, coroutineScope, navigator) }
        }
        .addPresenter<EventsScreen, EventsScreen.State> { _, _, _ ->
            presenterOf { EventsPresenter(eventPager, coroutineScope) }
        }
        .addPresenter<GalleryScreen, GalleryScreen.State> { _, _, _ ->
            presenterOf { GalleryPresenter(imageManager, syncManager) }
        }
        .addUi<HomeScreen, HomeScreen.State> { state, modifier ->
            HomeScreen(state, modifier, platformContext::reportFullyDrawn)
        }
        .addUi<EventsScreen, EventsScreen.State> { state, modifier ->
            EventsScreen(state, modifier)
        }
        .addUi<GalleryScreen, GalleryScreen.State> { state, modifier ->
            GalleryScreen(state, storageManager, modifier)
        }
        .build()
}
