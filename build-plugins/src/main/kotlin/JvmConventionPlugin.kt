import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@OptIn(ExperimentalKotlinGradlePluginApi::class)
internal class JvmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with<_, Unit>(target) {
        val jvmTargetString = libs.versions.kotlin.jvmTarget.get()

        extensions.configure<JavaPluginExtension> {
            val jvmTarget = JavaVersion.toVersion(jvmTargetString)
            sourceCompatibility = jvmTarget
            targetCompatibility = jvmTarget
        }

        tasks.withType<KotlinCompile> {
            val jvmTarget = JvmTarget.fromTarget(jvmTargetString)
            compilerOptions.jvmTarget.set(jvmTarget)
        }
    }
}
