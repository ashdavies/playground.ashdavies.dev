plugins {
    id("io.ashdavies.kotlin")
}

kotlin {
    commonMain.dependencies {
        api(libs.ktor.http)
    }
}
