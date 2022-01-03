import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

inline val PluginDependenciesSpec.`compose-multiplatform`: PluginDependencySpec
    get() = id("compose-multiplatform")
