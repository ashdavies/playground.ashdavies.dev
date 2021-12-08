package io.ashdavies.notion

import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand

@ExperimentalCli
internal abstract class CloseableSubcommand(
    name: String,
    actionDescription: String,
    private val closeable: Closeable = Closeable { },
) : Subcommand(name, actionDescription) {

    final override fun execute() = try {
        runBlocking { run() }
    } catch (exception: Throwable) {
        throw exception
    } finally {
        closeable.close()
    }

    abstract suspend fun run()
}
