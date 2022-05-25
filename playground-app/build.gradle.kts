
plugins {
    `multiplatform-library`
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":local-storage"))

                implementation(libs.bundles.ktor.client)
                implementation(libs.kuuuurt.multiplatform.paging)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.compose.foundation)
                implementation(libs.bundles.androidx.activity)

                with(libs.google.accompanist) {
                    implementation(placeholderMaterial)
                    implementation(swiperefresh)
                    implementation(systemuicontroller)
                }

                implementation(libs.bundles.google.firebase)
            }
        }
    }
}
