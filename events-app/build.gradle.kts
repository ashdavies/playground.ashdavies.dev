@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.VariantDimension

plugins {
    `multiplatform-application`
}

android {
    defaultConfig {
        val googleClientId by SystemProperty(VariantDimension::buildConfigField)
        val playgroundApiKey by SystemProperty(VariantDimension::buildConfigField)
    }
}

kotlin {
    val commonMain by sourceSets.getting {
        dependencies {
            implementation(project(":auth-oauth"))
            implementation(project(":local-remote"))
            implementation(project(":local-storage"))
            implementation(project(":playground-app"))

            implementation(libs.alialbaali.kamel)
            implementation(libs.bundles.arkivanov.decompose)
            implementation(libs.kuuuurt.multiplatform.paging)
            implementation(libs.sqldelight.coroutines.extensions)
        }
    }

    val androidMain by sourceSets.getting {
        dependencies {
            implementation(libs.bundles.androidx.activity)
            implementation(libs.bundles.androidx.paging)
            implementation(libs.bundles.androidx.viewmodel)

            with(libs.google.accompanist) {
                implementation(flowlayout)
                implementation(placeholderMaterial)
                implementation(swiperefresh)
            }

            implementation(libs.bundles.google.firebase)
        }
    }

    val jvmMain by sourceSets.getting {
        dependencies {
            implementation(libs.bundles.androidx.paging)
        }
    }
}
