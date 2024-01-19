plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.locale"
}

kotlin {
    commonMain.dependencies {
        implementation(compose.runtime)

        implementation(libs.jetbrains.kotlinx.serialization.core)
        implementation(libs.essenty.parcelable)
        implementation(libs.ktor.http)
    }
}
