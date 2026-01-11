import org.gradle.api.plugins.PluginAware
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.apply
import org.gradle.plugin.use.PluginDependency

internal fun PluginAware.apply(plugin: Provider<PluginDependency>): Unit = with(plugin.get()) {
    apply(plugin = pluginId)
}
