plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")

    alias(libs.plugins.metro)
}

kotlin {
    android.namespace = "dev.ashdavies.analytics"

    sourceSets.androidMain.dependencies {
        implementation(dependencies.platform(libs.google.firebase.bom))
        implementation(libs.google.firebase.analytics)
        implementation(libs.google.firebase.crashlytics)
    }
}

metro {
    generateContributionProviders = true
}
