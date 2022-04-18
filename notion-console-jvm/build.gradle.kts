import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.jakewharton.mosaic") version "0.1.0"
    kotlin("jvm")
    application
}

application {
    mainClass.set("io.ashdavies.playground.MainKt")
}

dependencies {
    implementation(project(":notion-console"))
}

tasks.withType<Jar> {
    archiveFileName.set("notion-console-compose.jar")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += listOf("-P", "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true")
        jvmTarget = "11"
        useFir = true
    }
}
