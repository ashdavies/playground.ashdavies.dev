package io.ashdavies.party

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

@Composable
@ExperimentalMaterial3Api
internal fun BottomSheetScaffold(
    sheetContent: @Composable ColumnScope.() -> Unit,
    topBar: @Composable () -> Unit = { },
    bottomBar: @Composable () -> Unit = { },
    floatingActionButton: @Composable () -> Unit = { },
    showDragHandle: Boolean = true,
    isExpanded: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
    val bottomSheetState = rememberStandardBottomSheetState(SheetValue.Hidden, skipHiddenState = false)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState)

    LaunchedEffect(isExpanded) {
        when (isExpanded) {
            true -> bottomSheetState.expand()
            false -> bottomSheetState.hide()
        }
    }

    val sheetDragHandle: @Composable (() -> Unit)? = when (showDragHandle) {
        true -> run { @Composable { BottomSheetDefaults.DragHandle() } }
        false -> null
    }

    BottomSheetScaffold(
        sheetContent = sheetContent,
        sheetDragHandle = sheetDragHandle,
        modifier = modifier,
        scaffoldState = scaffoldState,
    ) {
        Scaffold(
            topBar = topBar,
            bottomBar = bottomBar,
            floatingActionButton = floatingActionButton,
            content = content,
        )
    }
}
