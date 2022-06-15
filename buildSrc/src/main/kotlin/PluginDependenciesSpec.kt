@file:Suppress("UnstableApiUsage")

import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependency
import org.gradle.plugin.use.PluginDependencySpec

val PluginDependenciesSpec.`apollo-graphql`: PluginDependencySpec
    get() = id("apollo.graphql")

val PluginDependenciesSpec.`cloud-function`: PluginDependencySpec
    get() = id("cloud.function")

val PluginDependenciesSpec.`multiplatform-application`: PluginDependencySpec
    get() = multiplatform("application")

val PluginDependenciesSpec.`multiplatform-library`: PluginDependencySpec
    get() = multiplatform("library")

val PluginDependenciesSpec.`multiplatform-sql`: PluginDependencySpec
    get() = multiplatform("sql")

fun PluginDependenciesSpec.multiplatform(name: String): PluginDependencySpec =
    id("multiplatform.$name")

@Deprecated("Use build plugins")
@Suppress("DeprecatedCallableAddReplaceWith")
fun PluginDependenciesSpec.id(dependency: Provider<PluginDependency>) =
    id(dependency.get().pluginId)
