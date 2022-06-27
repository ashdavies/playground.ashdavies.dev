
plugins {
    `multiplatform-library`
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":local-remote"))
                implementation(project(":local-storage"))

                implementation(libs.bundles.ktor.client)
                implementation(libs.kuuuurt.multiplatform.paging)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.compose.foundation)
                implementation(libs.bundles.androidx.activity)
                implementation(libs.bundles.google.firebase)
                implementation(libs.jetbrains.kotlinx.coroutines.play)

                with(libs.google.accompanist) {
                    implementation(placeholderMaterial)
                    implementation(swiperefresh)
                    implementation(systemuicontroller)
                }
            }
        }
    }
}
