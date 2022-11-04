plugins {
    id("io.ashdavies.library")
}

android {
    namespace = "io.ashdavies.playground"
}

kotlin {
    val commonMain by sourceSets.dependencies {
        implementation(projects.appCheck.appCheckClient)

        implementation(projects.localRemote)
        implementation(projects.localStorage)

        implementation(libs.bundles.ktor.client)
        implementation(libs.jetbrains.kotlinx.collections.immutable)
        implementation(libs.kuuuurt.multiplatform.paging)
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
