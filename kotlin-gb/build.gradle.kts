plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.locale"
}

kotlin {
    commonMain.dependencies {
        implementation(libs.essenty.parcelable)
        implementation(libs.ktor.http)
    }
}
