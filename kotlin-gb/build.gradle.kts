plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.locale"
}

kotlin {
    commonMain.dependencies {
        implementation(compose.runtime)

        implementation(libs.essenty.parcelable)
        implementation(libs.kotlinx.serialization.core)
        implementation(libs.ktor.http)
    }
}
