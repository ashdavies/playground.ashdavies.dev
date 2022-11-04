@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

// https://youtrack.jetbrains.com/issue/KTIJ-19369

plugins {
    id("io.ashdavies.application")

    alias(libs.plugins.cash.molecule)
}

kotlin {
    val commonMain by sourceSets.dependencies {
        implementation(projects.appCheck.appCheckClient)

        implementation(projects.localRemote)
        implementation(projects.playgroundApp)

        implementation(libs.bundles.ktor.client)
        implementation(libs.kuuuurt.multiplatform.paging)
    }

    val androidMain by sourceSets.dependencies {
        implementation(libs.bundles.androidx.activity)
        implementation(libs.bundles.google.firebase)

        implementation(libs.google.accompanist.placeholderMaterial)
        implementation(libs.google.accompanist.swiperefresh)
    }
}
