import org.jetbrains.compose.compose

plugins {
    id("org.jetbrains.compose")

    kotlin("plugin.serialization")
    kotlin("jvm")
}

dependencies {
    implementation(projects.composeLocals)

    implementation(compose.foundation)
    implementation(compose.runtime)

    implementation(libs.bundles.jetbrains.kotlinx)
    implementation(libs.bundles.ktor.server)
}
