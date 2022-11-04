@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.VariantDimension

plugins {
    id("io.ashdavies.application")
}

android {
    defaultConfig {
        val googleClientId by SystemProperty(VariantDimension::buildConfigField)
        val playgroundApiKey by SystemProperty(VariantDimension::buildConfigField)
    }
}

kotlin {
    val commonMain by sourceSets.dependencies {
        implementation(projects.appCheck.appCheckClient)

        implementation(projects.authOauth)
        implementation(projects.localRemote)
        implementation(projects.localStorage)
        implementation(projects.playgroundApp)

        implementation(libs.bundles.arkivanov.decompose)
        implementation(libs.bundles.ktor.client)

        implementation(libs.kuuuurt.multiplatform.paging)
        implementation(libs.sqldelight.coroutines.extensions)
    }

    val androidMain by sourceSets.dependencies {
        implementation(projects.firebaseCompose)

        implementation(libs.bundles.androidx.activity)
        implementation(libs.bundles.androidx.paging)
        implementation(libs.bundles.androidx.viewmodel)
        implementation(libs.bundles.google.firebase)
        implementation(libs.bundles.google.maps)

        with(libs.google) {
            implementation(accompanist.flowlayout)
            implementation(accompanist.placeholderMaterial)
            implementation(accompanist.swiperefresh)
            implementation(firebase.appcheck.playintegrity)
            implementation(firebase.appcheck)
        }
    }

    val androidDebug by sourceSets.dependencies {
        implementation(libs.google.firebase.appcheck.debug)
    }

    val jvmMain by sourceSets.dependencies {
        implementation(libs.bundles.androidx.paging)
    }
}
