package io.ashdavies.playground.common

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.BottomNavigation
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.ashdavies.playground.R
import io.ashdavies.playground.Route.Conferences
import io.ashdavies.playground.conferences.ConferencesScreen
import kotlinx.coroutines.FlowPreview

@Composable
@FlowPreview
@OptIn(ExperimentalFoundationApi::class)
internal fun MainScreen(context: Context = LocalContext.current) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons,
        )
    }

    PlaygroundTheme {
        ProvideWindowInsets {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(stringResource(R.string.upcoming)) },
                        backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.95f),
                        contentPadding = rememberInsetsPaddingValues(
                            LocalWindowInsets.current.statusBars,
                            applyBottom = false,
                        ),
                    )
                },

                bottomBar = {
                    BottomNavigation(
                        backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.95f),
                        contentPadding = rememberInsetsPaddingValues(
                            LocalWindowInsets.current.navigationBars
                        )
                    ) {
                        BottomNavigationItem(
                            selected = false,
                            onClick = { TODO() },
                            icon = { Icon(Icons.Default.Home, "Home") }
                        )
                        BottomNavigationItem(
                            selected = false,
                            onClick = { TODO() },
                            icon = { Icon(Icons.Default.Person, "Profile") }
                        )
                    }
                },
            ) {
                NavHost(
                    navController = rememberNavController(),
                    startDestination = Conferences,
                ) {
                    composable(Conferences) { ConferencesScreen() }
                }
            }
        }
    }
}