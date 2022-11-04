import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation

plugins {
    id("org.jetbrains.compose")
    kotlin("multiplatform")
    application
}

kotlin {
    val jvm = jvm()

    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }

        val commonMain by getting {
            dependencies {
                implementation(projects.authOauth)
                implementation(projects.localStorage)

                implementation(compose.foundation)
                implementation(compose.runtime)

                implementation(libs.bundles.ktor.client)
                implementation(libs.jetbrains.compose.runtime)
                implementation(libs.jetbrains.kotlinx.cli)
                implementation(libs.jetbrains.kotlinx.coroutines.core)
                implementation(libs.jetbrains.kotlinx.serialization.json)
                implementation(libs.jraf.klibnotion)
                implementation(libs.qos.logbackClassic)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)

                implementation("com.jakewharton.mosaic:mosaic-runtime:0.1.0") {
                    exclude("com.jakewharton.mosaic", "compose-runtime")
                }

                implementation(libs.jetbrains.kotlinx.coroutines.core)
            }
        }

        tasks.withType<JavaExec> {
            val compilation: KotlinJvmCompilation = jvm
                .compilations
                .getByName("main")

            val classes: ConfigurableFileCollection = files(
                compilation.runtimeDependencyFiles,
                compilation.output.allOutputs
            )

            classpath(classes)
        }
    }
}

configurations.all {
    resolutionStrategy {
        force(libs.fusesource.jansi)
    }
}

application {
    mainClass.set("io.ashdavies.notion.MainKt")
}
