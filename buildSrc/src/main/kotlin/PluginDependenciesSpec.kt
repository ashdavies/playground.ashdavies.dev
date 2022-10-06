@file:Suppress("UnstableApiUsage")

import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependency
import org.gradle.plugin.use.PluginDependencySpec

public fun PluginDependenciesSpec.resolve(provider: Provider<PluginDependency>): PluginDependencySpec {
    return alias(provider).apply(false)
}

public fun PluginDependenciesSpec.id(provider: Provider<PluginDependency>): PluginDependencySpec {
    return id(provider.get().pluginId)
}
