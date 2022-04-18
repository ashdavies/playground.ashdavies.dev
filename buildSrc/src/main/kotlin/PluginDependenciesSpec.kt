@file:Suppress("UnstableApiUsage")

import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependency
import org.gradle.plugin.use.PluginDependencySpec

val PluginDependenciesSpec.`multiplatform-application`: PluginDependencySpec
    get() = id("multiplatform-application")

val PluginDependenciesSpec.`multiplatform-library`: PluginDependencySpec
    get() = id("multiplatform-library")

val PluginDependenciesSpec.`multiplatform-sql`: PluginDependencySpec
    get() = id("multiplatform-sql")

@Deprecated("Use build plugins")
@Suppress("DeprecatedCallableAddReplaceWith")
fun PluginDependenciesSpec.id(dependency: Provider<PluginDependency>) =
    id(dependency.get().pluginId)
