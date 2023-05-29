import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("plugin.serialization")
    kotlin("multiplatform")
}

kotlin {
    explicitApi()
    jvm()

    commonMain.dependencies {
        implementation(libs.bundles.jetbrains.kotlinx)
    }

    commonTest.dependencies {
        implementation(libs.app.cash.turbine)
        implementation(libs.bundles.jetbrains.kotlin.test)
        implementation(libs.bundles.jetbrains.kotlinx)
    }

    jvmMain.dependencies {
        implementation(libs.jetbrains.kotlinx.coroutines.swing)
    }

    jvmToolchain(Playground.jvmTarget)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += Playground.freeCompilerArgs
    kotlinOptions.jvmTarget = "${Playground.jvmTarget}"
}
