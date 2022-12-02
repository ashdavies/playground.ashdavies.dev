@file:Suppress("DSL_SCOPE_VIOLATION") // https://github.com/gradle/gradle/issues/22797

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java-gradle-plugin")
}

dependencies {
    implementation(libs.jetbrains.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        create("compose-constructor") {
            implementationClass = "io.ashdavies.playground.gradle.ComposeConstructorSubplugin"
            displayName = "Compose Remember Constructor Compiler Plugin"
            description = displayName
            id = name
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
