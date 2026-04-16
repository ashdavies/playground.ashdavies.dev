import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        plugins.apply(libs.plugins.android.application)
        plugins.apply(libs.plugins.firebase.crashlytics)
        plugins.apply(libs.plugins.google.services)

        pluginManager.withPlugin(libs.plugins.android.application) {
            extensions.configure<ApplicationExtension> {
                val androidProjectConvention = androidProjectConventionProvider.get()
                
                defaultConfig {
                    compileSdk = androidProjectConvention
                        .compileSdkVersion
                        .toInt()

                    minSdk = androidProjectConvention
                        .minSdkVersion
                        .toInt()
                }
            }
        }
    }
}
