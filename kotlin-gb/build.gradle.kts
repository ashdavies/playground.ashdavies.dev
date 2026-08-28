plugins {
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.kotlinx.serialization.core)
        implementation(libs.ktor.http)
    }
}
