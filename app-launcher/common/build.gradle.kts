plugins {
    id("com.android.library")
    id("io.ashdavies.android")
    id("io.ashdavies.kotlin")
}

android {
    namespace = "io.ashdavies.common"
}

kotlin {
    val commonMain by sourceSets.dependencies {
        implementation(compose.materialIconsExtended)
        implementation(projects.dominionApp)
        implementation(projects.eventsApp)
    }

    val androidMain by sourceSets.dependencies {
        implementation(libs.bundles.androidx.activity)
    }
}
