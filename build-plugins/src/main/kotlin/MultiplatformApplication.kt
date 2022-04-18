import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal class MultiplatformApplication : Plugin<Project> {
    override fun apply(target: Project): Unit = target.run {
        plugins.apply("com.android.application")
        plugins.apply("kotlin-parcelize")
        plugins.apply("org.jetbrains.compose")
        plugins.apply("org.jetbrains.kotlin.multiplatform")
        plugins.apply("org.jetbrains.kotlin.plugin.serialization")

        extensions.configure<BaseAppModuleExtension>(CommonExtension<*, *, *, *>::configureCommon)
        extensions.configure<KotlinMultiplatformExtension> { configureKotlinMultiplatform(target) }
        tasks.withType(KotlinCompile::configureKotlin)
    }
}
