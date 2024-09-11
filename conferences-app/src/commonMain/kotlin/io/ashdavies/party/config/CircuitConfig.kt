package io.ashdavies.party.config

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.Pager
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
internal fun ParentNavHost(
    platformContext: PlatformContext,
    startDestination: HomeScreen,
    navController: NavHostController = rememberNavController(),
    playgroundDatabase: PlaygroundDatabase = LocalTransacter.current as PlaygroundDatabase,
    coroutineScope: CoroutineScope = rememberRetainedCoroutineScope(),
) {
    val identityManager = IdentityManager(platformContext, playgroundDatabase.credentialQueries)

    NavHost(navController, startDestination.name) {
        composable("home") {
            HomeScreen(
                state = HomePresenter(identityManager, coroutineScope) {
                    navController.navigate(it.name)
                },
                context = platformContext,
                onFullyDrawn = { platformContext.reportFullyDrawn() },
            )
        }
    }
}

@Composable
internal fun ChildNavHost(
    platformContext: PlatformContext,
    startDestination: HomeScreen,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    eventPager: Pager<String, DatabaseEvent> = rememberEventPager(),
    playgroundDatabase: PlaygroundDatabase = LocalTransacter.current as PlaygroundDatabase,
    coroutineScope: CoroutineScope = rememberRetainedCoroutineScope(),
) {
    val imageManager = ImageManager(platformContext, playgroundDatabase.imageQueries)
    val inMemoryHttpClient = HttpClient(inMemoryHttpClientEngine(), DefaultHttpConfiguration)
    val syncManager = SyncManager(inMemoryHttpClient, File::readChannel)
    val storageManager = StorageManager(PathProvider(platformContext))

    NavHost(
        navController = navController,
        startDestination = startDestination.name,
        modifier = modifier,
    ) {
        composable("events") {
            EventsScreen(
                state = EventsPresenter(
                    eventPager = eventPager,
                    coroutineScope = coroutineScope,
                ),
            )
        }

        composable("gallery") {
            GalleryScreen(
                state = GalleryPresenter(
                    imageManager = imageManager,
                    syncManager = syncManager,
                ),
                manager = storageManager,
            )
        }
    }
}
