package io.ashdavies.notion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
@ExperimentalCli
internal fun rememberSubcommand(
    name: String,
    actionDescription: String,
    parser: ArgParser = LocalArgParser.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    block: suspend CoroutineScope.(Subcommand) -> Unit = { },
): Subcommand = remember(name, actionDescription) {
    Subcommand(name, actionDescription) {
        coroutineScope.launch { block(this@Subcommand) }
    }.also { parser.subcommands(it) }
}

@ExperimentalCli
private fun Subcommand(
    name: String,
    actionDescription: String,
    block: Subcommand.() -> Unit = { }
) = object : Subcommand(name, actionDescription) {
    override fun execute() = block()
}
