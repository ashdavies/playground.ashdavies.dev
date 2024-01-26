plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.locale"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.parcelableSupport)

        implementation(libs.kotlinx.serialization.core)
        implementation(libs.ktor.http)
    }
}
