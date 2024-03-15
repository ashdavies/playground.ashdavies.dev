plugins {
    id("io.ashdavies.kotlin")
}

kotlin {
    commonMain.dependencies {
        implementation(projects.parcelableSupport)

        implementation(libs.kotlinx.serialization.core)
        implementation(libs.ktor.http)
    }
}
