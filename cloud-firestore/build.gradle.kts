plugins {
    id("io.ashdavies.kotlin")
}

kotlin {
    jvmMain.dependencies {
        implementation(dependencies.platform(libs.google.cloud.bom))
        implementation(libs.google.cloud.firestore)
        implementation(libs.jetbrains.kotlinx.coroutines.core)
        implementation(libs.jetbrains.kotlinx.serialization.json)
    }
}
