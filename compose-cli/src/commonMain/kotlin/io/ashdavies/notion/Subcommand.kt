package io.ashdavies.notion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@ExperimentalCli
public fun ArgParser.Subcommand(
    name: String,
    actionDescription: String = name.replaceFirstChar { it.titlecase() },
    onExecute: suspend CoroutineScope.() -> Unit = { },
    content: @Composable () -> Unit = { },
) {
    val coroutineScope = rememberCoroutineScope()

    val subcommand = remember(name, actionDescription) {
        object : Subcommand(name, actionDescription) {
            override fun execute() {
                coroutineScope.launch { onExecute() }
            }
        }
    }

    LaunchedEffect(subcommand) {
        subcommands(subcommand)
    }

    content()
}