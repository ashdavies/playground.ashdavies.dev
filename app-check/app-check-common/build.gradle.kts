plugins {
    id("io.ashdavies.kotlin")
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(libs.ktor.http)
    }
}
