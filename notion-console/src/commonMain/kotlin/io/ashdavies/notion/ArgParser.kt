package io.ashdavies.notion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.SingleNullableOption
import kotlinx.coroutines.CoroutineScope

private const val ProgramName = "notion"

internal val LocalArgParser = staticCompositionLocalOf {
    ArgParser(ProgramName)
}

internal inline fun <reified T : Enum<T>> ArgParser.choice(
    fullName: String,
    description: String
): SingleNullableOption<T> = option(
    description = description,
    type = ArgType.Choice(),
    fullName = fullName,
)

internal fun ArgParser.string(
    fullName: String,
    description: String
): SingleNullableOption<String> = option(
    description = description,
    type = ArgType.String,
    fullName = fullName,
)

@Composable
@ExperimentalCli
internal fun ArgParser(
    name: String,
    actionDescription: String,
    parser: ArgParser = LocalArgParser.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable () -> Unit = { },
) = CompositionLocalProvider(
    LocalArgParser provides rememberSubcommand(
        actionDescription = actionDescription,
        coroutineScope = coroutineScope,
        parser = parser,
        name = name,
    ),
    content = content
)
