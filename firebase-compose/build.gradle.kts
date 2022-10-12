plugins {
    id("io.ashdavies.library")
}

kotlin {
    val commonMain by dependencies {
        implementation(project(":compose-locals"))
    }

    val androidMain by dependencies {
        implementation(libs.bundles.google.firebase)
    }
}
