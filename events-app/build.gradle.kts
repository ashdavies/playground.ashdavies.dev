import com.android.build.api.dsl.VariantDimension

plugins {
    id("com.android.library")
    id("io.ashdavies.library")
}

android {
    defaultConfig {
        val googleClientId by SystemProperty(VariantDimension::buildConfigField)
        val playgroundApiKey by SystemProperty(VariantDimension::buildConfigField)
    }

    namespace = "io.ashdavies.events"
}

kotlin {
    val commonMain by sourceSets.dependencies {
        implementation(projects.appCheck.appCheckClient)
        implementation(projects.pagingCompose)

        implementation(projects.authOauth)
        implementation(projects.localRemote)
        implementation(projects.localStorage)
        implementation(projects.playgroundApp)

        implementation(libs.bundles.arkivanov.decompose)
        implementation(libs.bundles.ktor.client)

        implementation(libs.sqldelight.coroutines.extensions)
    }

    val androidMain by sourceSets.dependencies {
        implementation(projects.firebaseCompose)

        with (libs.bundles) {
            implementation(androidx.activity)
            implementation(androidx.viewmodel)
            implementation(google.firebase)
            implementation(google.maps)
        }

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
}
