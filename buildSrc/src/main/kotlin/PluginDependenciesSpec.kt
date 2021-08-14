@file:Suppress("ObjectPropertyName")

import ProjectDependencies.AnvilVersion
import ProjectDependencies.BenManesVersions
import ProjectDependencies.KotlinVersion
import ProjectDependencies.Ktlint
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.version
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

private val KotlinVersion: String?
    get() = ProjectDependencies
        .JetBrains
        .Kotlin
        .version

private val SqlDelightVersion: String?
    get() = ProjectDependencies
        .Square
        .SqlDelight
        .version

val PluginDependenciesSpec.`android-application`: PluginDependencySpec
    get() = id("com.android.application")

val PluginDependenciesSpec.`android-library`: PluginDependencySpec
    get() = id("com.android.library")

val PluginDependenciesSpec.anvil: PluginDependencySpec
    get() = id("com.squareup.anvil") version AnvilVersion

val PluginDependenciesSpec.apollo: PluginDependencySpec
    get() = id("com.apollographql.apollo")

val PluginDependenciesSpec.`kotlin-android`: PluginDependencySpec
    get() = kotlin("android")

val PluginDependenciesSpec.`kotlin-jvm`: PluginDependencySpec
    get() = kotlin("jvm")

val PluginDependenciesSpec.`kotlin-multiplatform`: PluginDependencySpec
    get() = kotlin("multiplatform")

val PluginDependenciesSpec.ktlint: PluginDependencySpec
    get() = id("org.jlleitschuh.gradle.ktlint") version Ktlint

val PluginDependenciesSpec.serialization: PluginDependencySpec
    get() = kotlin("plugin.serialization") version KotlinVersion

val PluginDependenciesSpec.sqldelight: PluginDependencySpec
    get() = id("com.squareup.sqldelight")

val PluginDependenciesSpec.versions: PluginDependencySpec
    get() = id("com.github.ben-manes.versions") version BenManesVersions
