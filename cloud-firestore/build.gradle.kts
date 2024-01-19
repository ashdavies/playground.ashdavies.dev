plugins {
    id("io.ashdavies.kotlin")
}

kotlin {
    jvmMain.dependencies {
        implementation(dependencies.platform(libs.google.cloud.bom))
        implementation(libs.google.cloud.firestore)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.serialization.json)
    }
}
