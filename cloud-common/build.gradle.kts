plugins {
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.asgService)
        implementation(projects.httpCommon)

        implementation(libs.kotlinx.datetime)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.squareup.okio)
    }

    sourceSets.commonTest.dependencies {
        implementation(projects.fixtures.events)

        implementation(libs.kotlin.test)
    }
}

// Test bundle fails to load as fixtures:events -> event-common -> core:ui:scaffold pulls in Skiko,
// which is not bundled without the Compose plugin ("Can't resolve './skiko.mjs'"),
// so no tests are discovered and the runner disconnects.
tasks.named<AbstractTestTask>("wasmJsBrowserTest") {
    enabled = false
}
