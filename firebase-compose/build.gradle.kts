plugins {
    id("com.android.library")
    id("io.ashdavies.kotlin")
}

android {
    namespace = "io.ashdavies.firebase.compose"
}

kotlin {
    val commonMain by sourceSets.dependencies {
        implementation(projects.composeLocals)
    }

    val androidMain by sourceSets.dependencies {
        implementation(libs.bundles.google.firebase)
    }
}
