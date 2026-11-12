plugins {
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(projects.asgService)

        implementation(libs.squareup.okio)
    }

    sourceSets.commonTest.dependencies {
        implementation(projects.fixtures.events)

        implementation(libs.kotlin.test)
    }
}
