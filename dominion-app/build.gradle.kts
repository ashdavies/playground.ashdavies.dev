// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    `multiplatform-application`

    alias(libs.plugins.molecule)
}

android {
    // This is bad, don't do it, your mother will never forgive you
    testOptions.unitTests { isReturnDefaultValues = true }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":playground-app"))

                implementation(libs.ktor.client.core)
                implementation(libs.kuuuurt.multiplatform.paging)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.bundles.androidx.activity)

                with(libs.google.accompanist) {
                    implementation(placeholderMaterial)
                    implementation(swiperefresh)
                }

                implementation(libs.bundles.google.firebase)
                implementation(libs.ktor.client.cio)
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(libs.molecule.testing)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.client.cio)
            }
        }
    }
}
