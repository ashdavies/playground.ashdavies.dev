plugins {
    id("com.android.library")
    id("io.ashdavies.android")
    id("io.ashdavies.kotlin")
}

android {
    namespace = "io.ashdavies.base"
}

kotlin {
    val commonMain by sourceSets.dependencies {
        implementation(projects.appCheck.appCheckClient)

        implementation(projects.localRemote)
        implementation(projects.localStorage)

        implementation(libs.bundles.ktor.client)
        implementation(libs.jetbrains.kotlinx.collections.immutable)
    }

    val androidMain by sourceSets.dependencies {
        implementation(projects.firebaseCompose)

        implementation(libs.androidx.compose.foundation)
        implementation(libs.bundles.androidx.activity)
        implementation(libs.jetbrains.kotlinx.coroutines.play)

        with(libs.google) {
            implementation(accompanist.placeholderMaterial)
            implementation(accompanist.swiperefresh)
            implementation(accompanist.systemuicontroller)
        }
    }
}
