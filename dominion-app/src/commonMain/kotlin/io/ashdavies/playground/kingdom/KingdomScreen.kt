package io.ashdavies.playground.kingdom

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import io.ashdavies.playground.DominionExpansion
import io.ashdavies.playground.DominionRoot
import io.ashdavies.playground.LargeTopAppBar
import io.ashdavies.playground.rememberInsetsPaddingValues

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun KingdomScreen(child: DominionRoot.Child.Kingdom) {
    val scrollBehavior = remember { TopAppBarDefaults.enterAlwaysScrollBehavior() }

    Scaffold(
        topBar = { KingdomTopBar(child.expansion, scrollBehavior) { child.navigateToExpansion() } },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val list = (0..75).map { it.toString() }
            items(count = list.size) {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = list[it],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                )
            }
        }
    }
}

@Composable
private fun KingdomTopBar(
    expansion: DominionExpansion,
    scrollBehavior: TopAppBarScrollBehavior,
    contentPadding: PaddingValues = rememberInsetsPaddingValues(applyBottom = false),
    modifier: Modifier = Modifier,
    onBack: () -> Unit = { }
) {
    Surface(
        tonalElevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier,
    ) {
        LargeTopAppBar(
            title = { Text(expansion.name) },
            navigationIcon = {
                /*Image(
                    contentDescription = expansion.name,
                    modifier = Modifier.fillMaxWidth(),
                    urlString = expansion.image,
                )*/
                BackIconButton(onBack)
            },
            scrollBehavior = scrollBehavior,
            modifier = modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(contentPadding)
        )
    }
}

@Composable
private fun BackIconButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Image(
            painter = rememberVectorPainter(Icons.Filled.ArrowBack),
            contentDescription = null,
        )
    }
}
