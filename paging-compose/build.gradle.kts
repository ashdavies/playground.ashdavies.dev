@file:Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    alias(libs.plugins.johnrengelman.shadow)
    id("io.ashdavies.library")
    id("io.ashdavies.aar")
}

android {
    namespace = "io.ashdavies.paging"
}

kotlin {
    val commonMain by sourceSets.dependencies {
        api(libs.cash.paging.common)
    }

    val androidMain by sourceSets.dependencies {
        api(libs.bundles.androidx.paging)
    }

    val jvmMain by sourceSets.dependencies {
        api(libs.androidx.paging.compose)
    }
}

val repackageJar by tasks.creating(ShadowJar::class.java) {
    dependencies { include(dependency(libs.androidx.paging.compose.get())) }
    configurations = listOf(project.configurations["jvmRuntimeClasspath"])
    from(kotlin.jvm().compilations["main"].output)
}

