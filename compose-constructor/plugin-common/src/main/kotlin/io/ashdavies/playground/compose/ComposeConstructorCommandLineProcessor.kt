package io.ashdavies.playground.compose

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor

@AutoService(CommandLineProcessor::class)
internal class ComposeConstructorCommandLineProcessor : CommandLineProcessor {
    override val pluginId: String = "io.ashdavies.playground.compose-constructor"
    override val pluginOptions: Collection<AbstractCliOption> = emptyList()
}
