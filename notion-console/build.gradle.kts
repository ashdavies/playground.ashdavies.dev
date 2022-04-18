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
        val commonMain by getting {
            dependencies {
                implementation(project(":auth-oauth"))
                implementation(project(":local-storage"))

                implementation(compose.runtime)

                implementation(libs.jetbrains.kotlinx.cli)
                implementation(libs.jetbrains.kotlinx.coroutinesCore)
                implementation(libs.jetbrains.kotlinx.serializationJson)
                implementation(libs.jraf.klibnotion)
                implementation(libs.ktor.server.core)
                implementation(libs.qos.logbackClassic)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)

                implementation("com.jakewharton.mosaic:mosaic-runtime:0.1.0") {
                    exclude("com.jakewharton.mosaic", "compose-runtime")
                }

                implementation(libs.jetbrains.kotlinx.coroutinesCore)
                implementation(libs.ktor.server.cio)
                implementation(libs.ktor.server.core)
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
        force(libs.fuseSource.jansi)
    }
}

application {
    mainClass.set("io.ashdavies.notion.MainKt")
}
