plugins {
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")

    kotlin("multiplatform")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(compose.runtime)
    }

    jvm()
}
