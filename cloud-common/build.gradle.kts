import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("io.ashdavies.kotlin")
}

kotlin {
    explicitApiWarning()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.asgService)
            implementation(projects.httpCommon)

            implementation(libs.kotlinx.serialization.json)
            implementation(libs.squareup.okio)
        }
    }
}
