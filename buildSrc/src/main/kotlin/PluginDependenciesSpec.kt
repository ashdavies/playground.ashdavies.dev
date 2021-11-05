import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependency

@Suppress("UnstableApiUsage")
fun PluginDependenciesSpec.id(dependency: Provider<PluginDependency>) =
    id(dependency.get().pluginId)
