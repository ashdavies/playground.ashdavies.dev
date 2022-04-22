plugins {
    `multiplatform-library`
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":compose-local"))
                implementation(project(":local-storage"))

                with(libs.ktor.client) {
                    implementation(contentNegotiation)
                    implementation(core)
                    implementation(json)
                    implementation(logging)
                }

                implementation(libs.ktor.serialization.json)
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
    }
}
