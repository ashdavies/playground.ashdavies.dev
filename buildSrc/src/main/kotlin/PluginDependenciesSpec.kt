@file:Suppress("UnstableApiUsage")

import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependency

fun PluginDependenciesSpec.id(dependency: Provider<PluginDependency>) =
    id(dependency.get().pluginId)

fun PluginDependenciesSpec.group(dependency: Provider<MinimalExternalModuleDependency>) =
    id(dependency.get().module.group)
