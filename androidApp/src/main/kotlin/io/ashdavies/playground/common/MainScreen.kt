package io.ashdavies.playground.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.ashdavies.playground.R
import io.ashdavies.playground.Route.Conferences
import io.ashdavies.playground.conferences.ConferencesScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@Preview
@Composable
@FlowPreview
@ExperimentalCoroutinesApi
fun MainScreen() {
    val navController: NavHostController = rememberNavController()
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

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
                        backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.95f),
                        title = { Text(stringResource(R.string.application)) },
                        modifier = Modifier.padding(rememberInsetsPaddingValues(
                            insets = LocalWindowInsets.current.statusBars,
                            applyBottom = false
                        )),
                    )
                },
                bottomBar = {
                    BottomNavigation(
                        backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.95f),
                        modifier = Modifier.padding(rememberInsetsPaddingValues(
                            insets = LocalWindowInsets.current.navigationBars
                        )),
                    ) {
                        BottomNavigationItem(
                            selected = false,
                            onClick = { TODO() },
                            icon = { Icon(Icons.Default.Home, "Home") }
                        )
                    }
                }
            ) { paddingValues ->
                NavHost(
                    modifier = Modifier.padding(paddingValues),
                    startDestination = Conferences,
                    navController = navController,
                ) {
                    composable(Conferences) { ConferencesScreen() }
                }
            }
        }
    }
}