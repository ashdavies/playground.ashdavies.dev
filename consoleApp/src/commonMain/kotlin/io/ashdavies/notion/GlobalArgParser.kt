package io.ashdavies.notion

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default

private const val LOG_LEVEL_DESCRIPTION = "Logging log level"

internal class GlobalArgParser(programName: String) : ArgParser(programName) {

    val logLevel: LogLevel by option(
        description = LOG_LEVEL_DESCRIPTION,
        type = ArgType.Choice<LogLevel>(),
        fullName = "log_level",
    ).default(LogLevel.NONE)
}
