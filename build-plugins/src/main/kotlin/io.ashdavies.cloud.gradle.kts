import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.compose")

    kotlin("plugin.serialization")
    kotlin("jvm")
}

compose {
    val composeCompiler = libs.jetbrains.compose.compiler.get()
    kotlinCompilerPlugin.set("$composeCompiler")
}

dependencies {
    implementation(compose.runtime)
    implementation(libs.jetbrains.kotlinx.serialization.core)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
    val jvmTargetVersion = libs.versions.kotlin.jvmTarget.get()
    kotlinOptions.jvmTarget = jvmTargetVersion
}
