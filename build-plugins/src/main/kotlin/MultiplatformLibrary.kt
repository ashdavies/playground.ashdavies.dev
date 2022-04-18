import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal class MultiplatformLibrary : Plugin<Project> {
    override fun apply(target: Project): Unit = target.run {
        plugins.apply("com.android.library")
        plugins.apply("org.jetbrains.compose")
        plugins.apply("org.jetbrains.kotlin.multiplatform")
        plugins.apply("org.jetbrains.kotlin.plugin.serialization")

        extensions.configure<KotlinMultiplatformExtension> { configureKotlinMultiplatform(target) }
        extensions.configure<LibraryExtension>(CommonExtension<*, *, *, *>::configureCommon)
        tasks.withType(KotlinCompile::configureKotlin)
    }
}
