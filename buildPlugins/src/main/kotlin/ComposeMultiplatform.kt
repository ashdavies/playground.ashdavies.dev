@file:Suppress("UnstableApiUsage")

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class ComposeMultiplatform : Plugin<Project> {
    override fun apply(target: Project): Unit = target.run {
        plugins.apply("org.jetbrains.compose")
        plugins.apply("org.jetbrains.kotlin.multiplatform")

        configure<KotlinMultiplatformExtension> {
            sourceSets {
                val commonMain by getting {
                    dependencies {
                        implementation(compose.runtime)
                        implementation(compose.foundation)
                    }
                }
            }
        }
    }
}
