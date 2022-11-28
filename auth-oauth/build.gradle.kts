plugins {
    id("com.android.library")
    id("io.ashdavies.kotlin")
}

android {
    namespace = "io.ashdavies.oauth"
}

kotlin {
    val commonMain by sourceSets.dependencies {
        implementation(libs.bundles.ktor.client)
        implementation(libs.bundles.ktor.server)
    }
}
