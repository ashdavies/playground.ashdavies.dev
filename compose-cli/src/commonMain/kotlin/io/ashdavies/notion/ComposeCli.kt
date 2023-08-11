package io.ashdavies.notion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.cli.ArgParser

@Composable
@ComposeCli
public fun ComposeCli(
    name: String,
    args: Array<String> = emptyArray(),
    content: @Composable ArgParser.() -> Unit = { },
) {
    val argParser = ArgParser(name)
    argParser.content()

    LaunchedEffect(name) {
        argParser.parse(args)
    }
}
