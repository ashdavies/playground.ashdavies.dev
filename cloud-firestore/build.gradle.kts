plugins {
    id("io.ashdavies.kotlin")
}

configurations.all {
    resolutionStrategy.capabilitiesResolution.withCapability("${libs.google.guava.listenablefuture.get()}") {
        select(candidates.first { !it.id.displayName.contains("empty") })
    }
}

kotlin {
    jvmMain.dependencies {
        implementation(dependencies.platform(libs.google.cloud.bom))
        implementation(libs.google.cloud.firestore)
        implementation(libs.google.firebase.admin)
    }
}
