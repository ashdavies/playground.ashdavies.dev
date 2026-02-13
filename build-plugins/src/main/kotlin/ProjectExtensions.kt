import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.api.plugins.PluginAware
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.use.PluginDependency

// https://github.com/gradle/gradle/issues/15383
internal val Project.libs: LibrariesForLibs
    get() = extensions.getByType()

internal fun PluginContainer.apply(provider: Provider<PluginDependency>) {
    val plugin = provider.get()
    apply(plugin.pluginId)
}

public fun PluginAware.apply(plugin: Provider<PluginDependency>) {
    apply(plugin = plugin.get().pluginId)
}
