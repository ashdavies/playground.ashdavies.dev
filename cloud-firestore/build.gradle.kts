plugins {
    id("io.ashdavies.kotlin")
}

kotlin {
    jvmMain.dependencies {
        implementation(dependencies.platform(libs.google.cloud.bom))
        implementation(libs.google.cloud.firestore)
        implementation(libs.google.firebase.admin)
    }
}

configurations.all {
    resolutionStrategy.capabilitiesResolution.withCapability("com.google.guava:listenablefuture:1.0") {
        select(candidates.first { !it.id.displayName.contains("empty") })
    }
}
