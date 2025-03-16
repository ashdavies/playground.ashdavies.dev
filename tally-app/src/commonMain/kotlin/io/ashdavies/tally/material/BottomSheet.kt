package io.ashdavies.tally.material

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
@ExperimentalMaterial3Api
internal fun BottomSheetScaffold(
    sheetContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = { },
    bottomBar: @Composable () -> Unit = { },
    floatingActionButton: @Composable () -> Unit = { },
    showDragHandle: Boolean = true,
    isExpanded: Boolean = false,
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

@Composable
public fun BottomSheetItem(
    imageVector: ImageVector,
    onClick: () -> Unit,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Column(modifier = Modifier.width(IntrinsicSize.Max)) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            if (contentDescription != null) {
                Text(
                    text = contentDescription,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp),
                )
            }
        }
    }
}
