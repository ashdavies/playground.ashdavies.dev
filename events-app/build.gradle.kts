import com.android.build.api.dsl.VariantDimension

plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.parcelable")
    id("io.ashdavies.sql")
}

android {
    defaultConfig {
        val googleClientId by SystemProperty(VariantDimension::buildConfigField)
        val playgroundApiKey by SystemProperty(VariantDimension::buildConfigField)
    }

    namespace = "io.ashdavies.events"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.appCheck.appCheckClient)
        implementation(projects.composeLocals)

        implementation(projects.authOauth)
        implementation(projects.localRemote)
        implementation(projects.localStorage)
        implementation(projects.playgroundApp)

        implementation(libs.bundles.ktor.client)
        implementation(libs.bundles.paging.compose)
        implementation(libs.bundles.slack.circuit)

        implementation(libs.arkivanov.parcelable)
        implementation(libs.sqldelight.coroutines.extensions)
    }

    androidMain.dependencies {
        implementation(dependencies.platform(libs.google.firebase.bom))
        implementation(projects.firebaseCompose)

        with(libs.bundles) {
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
            implementation(firebase.auth.ktx)
        }
    }

    androidDebug.dependencies {
        implementation(libs.google.firebase.appcheck.debug)
    }
}

sqldelight {
    database("PlaygroundDatabase") {
        dependency(projects.localStorage.dependencyProject)
        packageName = "io.ashdavies.events"
    }
}
