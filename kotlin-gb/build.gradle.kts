plugins {
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(libs.kotlinx.serialization.core)
        api(libs.ktor.http)
    }
}
