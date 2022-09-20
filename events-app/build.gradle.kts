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
    val commonMain by dependencies {
        implementation(project(":auth-oauth"))
        implementation(project(":local-remote"))
        implementation(project(":local-storage"))
        implementation(project(":playground-app"))

        implementation(libs.bundles.arkivanov.decompose)
        implementation(libs.bundles.ktor.client)

        implementation(libs.kuuuurt.multiplatform.paging)
        implementation(libs.sqldelight.coroutines.extensions)
    }

    val androidMain by dependencies {
        implementation(libs.bundles.androidx.activity)
        implementation(libs.bundles.androidx.paging)
        implementation(libs.bundles.androidx.viewmodel)

        with(libs.google.accompanist) {
            implementation(flowlayout)
            implementation(placeholderMaterial)
            implementation(swiperefresh)
        }

        implementation(libs.bundles.google.firebase)
        implementation(libs.bundles.google.maps)
    }

    val jvmMain by dependencies {
        implementation(libs.bundles.androidx.paging)
    }
}
