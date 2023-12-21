plugins {
    id("io.ashdavies.default")

    alias(libs.plugins.squareup.wire)
}

android {
    namespace = "io.ashdavies.identity"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.platformSupport)
        api(libs.androidx.datastore.core)
    }

    androidMain.dependencies {
        implementation(libs.androidx.datastore.android)
    }
}

wire {
    kotlin { }
}
