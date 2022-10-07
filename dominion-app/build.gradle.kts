@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage") // https://youtrack.jetbrains.com/issue/KTIJ-19369

import org.jetbrains.kotlin.gradle.plugin.HasKotlinDependencies
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

plugins {
    id("io.ashdavies.application")

    alias(libs.plugins.cash.molecule)
}

kotlin {
    val commonMain by sourceSets.dependencies {
        implementation(project(":local-remote"))
        implementation(project(":playground-app"))

        implementation(libs.bundles.ktor.client)
        implementation(libs.kuuuurt.multiplatform.paging)
    }

    val androidMain by sourceSets.dependencies {
        implementation(libs.bundles.androidx.activity)
        implementation(libs.bundles.google.firebase)

        implementation(libs.google.accompanist.placeholderMaterial)
        implementation(libs.google.accompanist.swiperefresh)
    }

    val androidTest by sourceSets
}

fun <T : HasKotlinDependencies> NamedDomainObjectContainer<T>.dependencies(
    configuration: KotlinDependencyHandler.() -> Unit
) = getting { dependencies(configuration) }
