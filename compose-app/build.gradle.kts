plugins {
    `multiplatform-library`
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":compose-local"))
            }
        }

        val androidMain by getting {
            dependencies {
                with(libs.androidx) {
                    implementation(activityCompose)
                    implementation(activityKtx)
                }

                with(libs.google.accompanist) {
                    implementation(insets)
                    implementation(insetsUi)
                    implementation(placeholderMaterial)
                    implementation(swiperefresh)
                    implementation(systemuicontroller)
                }

                with(libs.google.firebase) {
                    implementation(analytics)
                    implementation(commonKtx)
                }
            }
        }
    }
}
