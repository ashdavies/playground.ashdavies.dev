import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.AppliedPlugin
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.plugins.PluginManager
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.use.PluginDependency

// https://github.com/gradle/gradle/issues/15383
internal val Project.libs: LibrariesForLibs
    get() = extensions.getByType()

internal fun PluginContainer.apply(
    provider: Provider<PluginDependency>,
) = apply(provider.get().pluginId)

internal fun PluginContainer.apply(
    provider: Provider<PluginDependency>,
    action: Plugin<*>.() -> Unit,
) = with(provider.get()) {
    apply(pluginId)
    withId(pluginId, action)
}

internal fun PluginManager.withPlugin(
    provider: Provider<PluginDependency>,
    action: AppliedPlugin.() -> Unit,
) = with(provider.get()) {
    withPlugin(pluginId, action)
}
