@file:Suppress("UnstableApiUsage")

import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependency
import org.gradle.plugin.use.PluginDependencySpec

val PluginDependenciesSpec.`playground-android-library`: PluginDependencySpec
    get() = id("playground-android-library")

val PluginDependenciesSpec.`playground-compose-multiplatform`: PluginDependencySpec
    get() = id("playground-compose-multiplatform")

@Deprecated("Use build plugins")
@Suppress("DeprecatedCallableAddReplaceWith")
fun PluginDependenciesSpec.id(dependency: Provider<PluginDependency>) =
    id(dependency.get().pluginId)
