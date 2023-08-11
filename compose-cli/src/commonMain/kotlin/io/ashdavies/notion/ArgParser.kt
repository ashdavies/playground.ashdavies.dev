package io.ashdavies.notion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.cli.ArgParser

internal val LocalArgParser = staticCompositionLocalOf<ArgParser> {
    error("No local provided for LocalArgParser")
}

@Composable
@ComposeCli
public fun ComposeCli(
    name: String,
    args: Array<String> = emptyArray(),
    content: @Composable () -> Unit = { },
) {
    val argParser = ArgParser(name)

    CompositionLocalProvider(
        LocalArgParser provides ArgParser(name),
        content = content,
    )

    LaunchedEffect(name) {
        argParser.parse(args)
    }
}
