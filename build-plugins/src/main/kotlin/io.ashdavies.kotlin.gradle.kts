import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.compose")
    // id("app.cash.molecule")

    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    explicitApiWarning()
    jvm()

    @OptIn(ExperimentalComposeLibrary::class)
    commonMain.dependencies {
        implementation(compose.foundation)
        implementation(compose.material3)
        implementation(compose.runtime)
        implementation(compose.uiTooling)
        implementation(compose.ui)

        implementation(libs.bundles.arkivanov.decompose)
        implementation(libs.bundles.jetbrains.kotlinx)
        implementation(libs.oolong)
    }

    commonTest.dependencies {
        implementation(libs.bundles.jetbrains.kotlin.test)
        implementation(libs.bundles.jetbrains.kotlinx)
        implementation(libs.app.cash.turbine)
    }

    jvmMain.dependencies {
        implementation(compose.desktop.currentOs)
        implementation(libs.jetbrains.kotlinx.coroutines.swing)
    }

    configurations.forEach {
        if (it.name.contains("jvm")) it.exclude(
            module = "kotlinx-coroutines-android",
            group = "org.jetbrains.kotlinx",
        )
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += Playground.freeCompilerArgs
    kotlinOptions.jvmTarget = Playground.jvmTarget
}
