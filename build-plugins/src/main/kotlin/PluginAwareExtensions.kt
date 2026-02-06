import org.gradle.api.plugins.PluginAware
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.apply
import org.gradle.plugin.use.PluginDependency

public fun PluginAware.apply(plugin: Provider<PluginDependency>) {
    apply(plugin = plugin.get().pluginId)
}
