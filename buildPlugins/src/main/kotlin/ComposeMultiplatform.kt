@file:Suppress("UnstableApiUsage")

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.hasPlugin
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMultiplatformPlugin

internal class ComposeMultiplatform : ProjectCommon() {
    override fun apply(target: Project): Unit = target.run {
        plugins.apply("org.jetbrains.compose")

        if (!plugins.hasPlugin(KotlinMultiplatformPlugin::class)) {
            plugins.apply("org.jetbrains.kotlin.multiplatform")
        }

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets {
                val commonMain by getting {
                    dependencies {
                        implementation(compose.runtime)
                        implementation(compose.foundation)
                    }
                }
            }
        }

        super.apply(target)
    }
}
