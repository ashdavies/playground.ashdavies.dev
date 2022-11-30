import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.compose")

    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    explicitApiWarning()
    jvm()

    sourceSets.all {
        languageSettings.optIn("kotlin.RequiresOptIn")
    }

    @Suppress("UNUSED_VARIABLE")
    @OptIn(ExperimentalComposeLibrary::class)
    val commonMain by sourceSets.dependencies {
        implementation(compose.foundation)
        implementation(compose.material3)
        implementation(compose.runtime)
        implementation(compose.uiTooling)
        implementation(compose.ui)

        implementation(libs.bundles.arkivanov.decompose)
        implementation(libs.bundles.jetbrains.kotlinx)
        implementation(libs.oolong)
    }

    @Suppress("UNUSED_VARIABLE")
    val commonTest by sourceSets.dependencies {
        implementation(libs.bundles.jetbrains.kotlin.test)
    }

    @Suppress("UNUSED_VARIABLE")
    val jvmMain by sourceSets.dependencies {
        implementation(compose.desktop.currentOs)
        implementation(libs.jetbrains.kotlinx.coroutines.swing)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += Playground.freeCompilerArgs
    kotlinOptions.jvmTarget = Playground.jvmTarget
}
