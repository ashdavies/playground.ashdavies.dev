plugins {
    id("io.ashdavies.kotlin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
        }

        jvmMain.dependencies {
            implementation(dependencies.platform(libs.google.cloud.bom))
            implementation(libs.google.cloud.firestore)
        }
    }
}
