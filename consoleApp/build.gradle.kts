// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation

plugins {
    id(libs.plugins.kotlin.multiplatform)
    id(libs.plugins.sqldelight)


    application
    alias(libs.plugins.serialization)
    group(libs.jetbrains.compose.gradlePlugin)
}

kotlin {
    val jvmTarget = jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":notionClient"))
                implementation(project(":sqlDriver"))

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)

                implementation(libs.qos.logbackClassic)
                implementation(libs.sqlDelight.coroutinesExtensions)
                implementation(libs.sqlDelight.runtime)
                implementation(libs.jetbrains.kotlinx.cli)
                implementation(libs.jetbrains.kotlinx.datetime)
                implementation(libs.jetbrains.kotlinx.coroutinesCore)
                implementation(libs.jetbrains.kotlinx.serializationJson)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)

                implementation(libs.sqlDelight.sqliteDriver)
                implementation(libs.jetbrains.kotlinx.coroutinesJdk)
            }
        }
    }

    tasks.withType<JavaExec> {
        val compilation: KotlinJvmCompilation = jvmTarget
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

sqldelight {
    database("AuthHistory") {
        packageName = "io.ashdavies.notion"
        dialect = "mysql"
    }
}
