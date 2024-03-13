import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.commonExtension(action: CommonExtension<*, *, *, *, *, *>.() -> Unit) {
    val androidApplicationPlugin = libs.plugins.android.application.get()
    pluginManager.withPlugin(androidApplicationPlugin.pluginId) {
        extensions.configure<BaseAppModuleExtension>(action)
    }

    val androidLibraryPlugin = libs.plugins.android.library.get()
    pluginManager.withPlugin(androidLibraryPlugin.pluginId) {
        extensions.configure<LibraryExtension>(action)
    }
}
