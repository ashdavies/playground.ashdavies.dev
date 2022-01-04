// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation

plugins {
    id("playground-compose-multiplatform")
    application

    alias(libs.plugins.serialization)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":localStorage"))

                implementation(libs.jetbrains.kotlinx.cli)
                implementation(libs.jetbrains.kotlinx.datetime)
                implementation(libs.jetbrains.kotlinx.coroutinesCore)
                implementation(libs.jetbrains.kotlinx.serializationJson)
                implementation(libs.jraf.klibnotion)
                implementation(libs.ktor.server.core)
                implementation(libs.qos.logbackClassic)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.jetbrains.kotlinx.coroutinesJdk)
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
