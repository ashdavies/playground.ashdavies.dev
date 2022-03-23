// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    application
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":local-storage"))
                implementation(compose.runtime)

                implementation(libs.jetbrains.kotlinx.cli)
                implementation(libs.jraf.klibnotion)
                implementation(libs.ktor.server.core)
                implementation(libs.qos.logbackClassic)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.server.cio)
            }
        }
    }

    tasks.withType<JavaExec> {
        val compilation: KotlinJvmCompilation = jvm()
            .compilations
            .getByName("main")

        val classes: ConfigurableFileCollection = files(
            compilation.runtimeDependencyFiles,
            compilation.output.allOutputs
        )

        classpath(classes)
    }
}

application {
    mainClass.set("io.ashdavies.notion.MainKt")
}

tasks.test {
    useJUnit()
}
