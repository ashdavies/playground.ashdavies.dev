plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

kotlin {
    android.namespace = "dev.ashdavies.routing"

    sourceSets.commonMain.dependencies {
        api(projects.core.http.common)
        api(libs.ktor.client.core)
    }
}
