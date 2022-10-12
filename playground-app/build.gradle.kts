plugins {
    id("io.ashdavies.library")
    id("android-manifest")
}

kotlin {
    val commonMain by dependencies {
        implementation(project(":app-check:app-check-client"))

        implementation(project(":local-remote"))
        implementation(project(":local-storage"))

        implementation(libs.bundles.ktor.client)
        implementation(libs.jetbrains.kotlinx.collections.immutable)
        implementation(libs.kuuuurt.multiplatform.paging)
    }

    val androidMain by dependencies {
        implementation(project(":firebase-compose"))

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
