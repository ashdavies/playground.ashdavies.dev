package io.ashdavies.notion.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import io.ashdavies.notion.kotlin.NotionScopeMarker
import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val ProgramName = "notion"

internal val LocalArgParser = staticCompositionLocalOf {
    ArgParser(ProgramName)
}

@Composable
@ExperimentalCli
@NotionScopeMarker
internal fun Subcommand(
    name: String,
    actionDescription: String = name.replaceFirstChar { it.titlecase() },
    onExecute: suspend CoroutineScope.(Subcommand) -> Unit = { },
    content: @Composable () -> Unit = { }
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val argParser: ArgParser = LocalArgParser.current

    val subcommand: Subcommand = rememberSubcommand(name, actionDescription) {
        coroutineScope.launch { onExecute(it) }
    }

    LaunchedEffect(subcommand) {
        argParser.subcommands(subcommand)
    }

    CompositionLocalProvider(
        LocalArgParser provides subcommand,
        content = content
    )
}

@Composable
@ExperimentalCli
private fun rememberSubcommand(
    name: String,
    actionDescription: String,
    block: (Subcommand) -> Unit
): Subcommand = remember(name, actionDescription) {
    object : Subcommand(name, actionDescription) {
        override fun execute() = block(this)
    }
}
