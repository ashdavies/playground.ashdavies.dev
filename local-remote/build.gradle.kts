@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.VariantDimension

plugins {
    `multiplatform-library`
}

android {
    defaultConfig {
        val clientName by SystemProperty(VariantDimension::buildConfigField) { "Ktor/${libs.versions.ktor.get()}" }
    }
}

kotlin {
    val commonMain by sourceSets.getting {
        dependencies {
            implementation(project(":local-storage"))
            implementation(libs.bundles.ktor.client)
        }
    }
}
