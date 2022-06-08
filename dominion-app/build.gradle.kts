@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage") // https://youtrack.jetbrains.com/issue/KTIJ-19369

plugins {
    `multiplatform-application`

    alias(libs.plugins.cash.molecule)
}

kotlin {
    val commonMain by sourceSets.getting {
        dependencies {
            implementation(project(":local-remote"))
            implementation(project(":playground-app"))

            implementation(libs.bundles.ktor.client)
            implementation(libs.kuuuurt.multiplatform.paging)
        }
    }

    sourceSets {
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
