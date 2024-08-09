import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.api.plugins.AppliedPlugin
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.configure

private val Project.androidApplicationPlugin
    get() = libs.plugins.android.application.get()

private val Project.androidLibraryPlugin
    get() = libs.plugins.android.library.get()

context(Project)
internal fun PluginManager.commonExtension(action: CommonExtension<*, *, *, *, *, *>.() -> Unit) {
    pluginManager.withPlugin(androidApplicationPlugin.pluginId) {
        extensions.configure<BaseAppModuleExtension>(action)
    }

    pluginManager.withPlugin(androidLibraryPlugin.pluginId) {
        extensions.configure<LibraryExtension>(action)
    }
}

context(Project)
internal fun PluginManager.withAndroidPlugin(action: AppliedPlugin.() -> Unit) {
    pluginManager.withPlugin(androidApplicationPlugin.pluginId, action)
    pluginManager.withPlugin(androidLibraryPlugin.pluginId, action)
}
