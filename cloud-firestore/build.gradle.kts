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
