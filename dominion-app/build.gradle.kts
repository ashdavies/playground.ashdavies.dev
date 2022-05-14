// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    `multiplatform-application`

    alias(libs.plugins.cash.molecule)
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

                implementation(libs.bundles.ktor.client)
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
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(libs.molecule.testing)
            }
        }
    }
}
