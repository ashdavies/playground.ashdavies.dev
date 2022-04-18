plugins {
    `multiplatform-application`
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kuuuurt.multiplatformPaging)
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

                implementation(libs.ktor.client.cio)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.client.cio)
            }
        }
    }
}
