@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal class AndroidApplication : ProjectCommon() {
    override fun apply(target: Project): Unit = target.run {
        plugins.apply("com.android.application")
        plugins.apply("org.jetbrains.kotlin.android")

        extensions.configure<BaseAppModuleExtension> {
            AndroidBase(target, this)

            sourceSets.configureEach {
                java.srcDirs("src/$name/kotlin")
            }
        }

        dependencies {
            with(libs.androidx.compose) {
                implementation(foundation)
                implementation(material)
                implementation(runtime)
                implementation(ui)
                implementation(uiTooling)
            }

            with(libs.google.accompanist) {
                implementation(coil)
                implementation(flowlayout)
                implementation(insets)
                implementation(insetsUi)
                implementation(placeholderMaterial)
                implementation(swiperefresh)
                implementation(systemuicontroller)
            }

            with(libs.jetbrains.kotlinx) {
                implementation(coroutinesAndroid)
                implementation(coroutinesCore)
            }

            with(libs.jetbrains) {
                testImplementation(kotlin.test)
                testImplementation(kotlin.testJunit)
                testImplementation(kotlinx.coroutinesTest)
            }
        }

        super.apply(target)
    }
}

private fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
    add("implementation", dependencyNotation)

private fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? =
    add("testImplementation", dependencyNotation)
