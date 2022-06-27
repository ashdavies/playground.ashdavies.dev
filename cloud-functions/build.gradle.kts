import org.jetbrains.compose.compose

plugins {
    id("org.jetbrains.compose")
    kotlin("jvm")
}

dependencies {
    implementation(project(":local-storage"))

    implementation(compose.foundation)
    implementation(compose.runtime)

    implementation(libs.bundles.jetbrains.kotlinx)
}
