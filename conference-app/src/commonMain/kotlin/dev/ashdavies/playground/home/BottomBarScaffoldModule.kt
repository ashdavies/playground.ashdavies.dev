package dev.ashdavies.playground.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Route
import com.slack.circuit.runtime.screen.Screen
import dev.ashdavies.config.RemoteConfig
import dev.ashdavies.config.getBoolean
import dev.ashdavies.playground.adaptive.ListDetailScaffoldScreen
import dev.ashdavies.playground.event.EventScreen
import dev.ashdavies.playground.gallery.GalleryScreen
import dev.ashdavies.playground.material.icons.EventList
import dev.ashdavies.playground.material.icons.EventUpcoming
import dev.ashdavies.playground.routes.RoutesScreen
import dev.ashdavies.playground.ui.Res
import dev.ashdavies.playground.ui.gallery
import dev.ashdavies.playground.ui.past_events
import dev.ashdavies.playground.ui.routes
import dev.ashdavies.playground.ui.upcoming_events
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
internal interface BottomBarScaffoldModule {

    @Provides
    suspend fun bottomBarScaffoldScreenMetadataPairs(remoteConfig: RemoteConfig): List<Pair<Screen, BottomBarScaffoldMetadata>> {
        val isGalleryEnabled = remoteConfig.getBoolean("gallery_enabled")
        val isRoutesEnabled = remoteConfig.getBoolean("routes_enabled")
        val isPastEventsEnabled = remoteConfig.getBoolean("past_events_enabled")

        return if (!isPastEventsEnabled && !isGalleryEnabled && !isRoutesEnabled) {
            emptyList()
        } else {
            buildList {
                add(
                    element = ListDetailScaffoldScreen(EventScreen.List()) to BottomBarScaffoldMetadata(
                        label = Res.string.upcoming_events,
                        icon = Icons.Outlined.EventUpcoming,
                    ),
                )

                if (isGalleryEnabled) {
                    add(
                        element = GalleryScreen to BottomBarScaffoldMetadata(
                            label = Res.string.gallery,
                            icon = Icons.Outlined.EventUpcoming,
                        ),
                    )
                }

                if (isRoutesEnabled) {
                    add(
                        element = RoutesScreen to BottomBarScaffoldMetadata(
                            label = Res.string.routes,
                            icon = Icons.Outlined.Route,
                        ),
                    )
                }

                if (isPastEventsEnabled) {
                    add(
                        element = EventScreen.Grid() to BottomBarScaffoldMetadata(
                            label = Res.string.past_events,
                            icon = Icons.Outlined.EventList,
                        ),
                    )
                }
            }
        }
    }
}
