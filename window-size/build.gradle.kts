plugins {
    `multiplatform-library`
}

kotlin {
    val commonMain by sourceSets.getting {
        dependencies {
            api(libs.androidx.compose.material3.windowSize)
        }
    }
}
