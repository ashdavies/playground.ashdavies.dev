plugins {
    id("io.ashdavies.kotlin")
    id("io.ashdavies.spotless")
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
