package io.ashdavies.playground.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.BottomNavigation
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.ashdavies.playground.R
import io.ashdavies.playground.Route
import io.ashdavies.playground.Route.Events
import io.ashdavies.playground.Route.Profile
import io.ashdavies.playground.compose.NavHost
import io.ashdavies.playground.compose.composable
import io.ashdavies.playground.compose.navigate
import io.ashdavies.playground.compose.routeAsState
import io.ashdavies.playground.events.EventsScreen
import io.ashdavies.playground.profile.ProfileScreen

@Composable
internal fun MainScreen() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !MaterialTheme.colors.isLight

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons,
        )
    }

    PlaygroundTheme {
        ProvideWindowInsets {
            val navController: NavHostController = rememberNavController()
            val currentRoute by navController.routeAsState(Events)

            Scaffold(
                topBar = { PlaygroundTopBar(currentRoute) },
                bottomBar = { PlaygroundBottomBar(navController, currentRoute) },
            ) { PlaygroundNavHost(navController) }
        }
    }
}

@Composable
private fun PlaygroundTopBar(currentRoute: Route) {
    TopAppBar(
        title = { Text(stringResource(currentRoute.title)) },
        backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.95f),
        contentPadding = rememberInsetsPaddingValues(
            LocalWindowInsets.current.statusBars,
            applyBottom = false,
        ),
    )
}

@Composable
private fun PlaygroundBottomBar(navController: NavController, currentRoute: Route) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.95f),
        contentPadding = rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.navigationBars
        )
    ) {
        BottomNavigationItem(
            isSelected = { it == currentRoute },
            onClick = { navController.navigate(it) },
            icon = Icons.Default.Home,
            route = Events,
        )

        BottomNavigationItem(
            isSelected = { it == currentRoute },
            onClick = { navController.navigate(it) },
            icon = Icons.Default.Person,
            route = Profile,
        )
    }
}

@Composable
private fun RowScope.BottomNavigationItem(
    isSelected: (Route) -> Boolean,
    onClick: (Route) -> Unit,
    icon: ImageVector,
    route: Route,
) {
    BottomNavigationItem(
        icon = { Icon(icon, stringResource(route.title)) },
        label = { Text(stringResource(route.title)) },
        onClick = { onClick(route) },
        selected = isSelected(route),
    )
}

@Composable
private fun PlaygroundNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Profile,
    ) {
        composable(Events) { EventsScreen() }
        composable(Profile) { ProfileScreen() }
    }
}

private val Route.title: Int
    @StringRes get() = when (this) {
        is Events -> R.string.upcoming
        is Profile -> R.string.profile
    }
