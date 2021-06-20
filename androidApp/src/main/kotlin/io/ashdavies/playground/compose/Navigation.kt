package io.ashdavies.playground.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.ashdavies.playground.Route

@Composable
internal fun NavHost(
    navController: NavHostController,
    startDestination: Route,
    builder: NavGraphBuilder.() -> Unit,
) = NavHost(
    navController = navController,
    startDestination = startDestination.name,
    builder = builder,
)

internal fun NavController.navigate(route: Route) =
    navigate(route.name)

internal fun NavGraphBuilder.composable(
    route: Route,
    content: @Composable (NavBackStackEntry) -> Unit,
) = composable(route.name, content = content)

private fun NavDestination.getRouteOrNull(): Route? {
    val routes: Map<String, Route> = Route.routes.associateBy { it.name }
    for (destination: NavDestination in hierarchy) {
        val route: Route? = routes[destination.route]
        if (route != null) return route
    }
    return null
}

@Composable
internal fun NavController.routeAsState(default: Route): State<Route> {
    val current: Route? = currentDestination?.getRouteOrNull()
    val state = remember { mutableStateOf(current ?: default) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            destination.getRouteOrNull()?.let { state.value = it }
        }

        addOnDestinationChangedListener(listener)
        onDispose { removeOnDestinationChangedListener(listener) }
    }

    return state
}