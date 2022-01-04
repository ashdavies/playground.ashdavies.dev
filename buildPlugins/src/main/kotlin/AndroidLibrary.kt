@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.LibraryExtension
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.hasPlugin
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMultiplatformPlugin

internal class AndroidLibrary : ProjectCommon() {
    override fun apply(target: Project): Unit = target.run {
        plugins.apply("com.android.library")

        if (!plugins.hasPlugin(KotlinMultiplatformPlugin::class)) {
            plugins.apply("org.jetbrains.kotlin.multiplatform")
        }

        configurations {
            create("testApi", "testDebugApi", "testReleaseApi")
        }

        extensions.configure<LibraryExtension> {
            AndroidBase(target, this)

            sourceSets.configureEach {
                manifest.srcFile("src/androidMain/AndroidManifest.xml")
            }
        }

        extensions.configure<KotlinMultiplatformExtension> {
            android()

            sourceSets {
                val androidMain by getting {
                    dependencies {
                        implementation(libs.jetbrains.kotlinx.coroutinesAndroid)
                        implementation(libs.jetbrains.kotlinx.coroutinesCore)
                    }
                }

                val androidTest by getting {
                    dependencies {
                        implementation(libs.jetbrains.kotlin.test)
                        implementation(libs.jetbrains.kotlin.testJunit)
                    }
                }
            }
        }

        super.apply(target)
    }

    /*
     * MPP does not work with AGP 7.0+: "Configuration with name 'testApi' not found."
     * https://youtrack.jetbrains.com/issue/KT-43944
     */
    private fun NamedDomainObjectContainer<*>.create(vararg names: String) {
        names.forEach { create(it) }
    }
}

