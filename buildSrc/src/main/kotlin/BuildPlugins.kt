@file:Suppress("ObjectPropertyName")

import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.create
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

val DependencyHandler.`batik-ext`: ExternalModuleDependency
    get() = create("org.apache.xmlgraphics", "batik-ext", "1.14")

val DependencyHandler.gradle: ExternalModuleDependency
    get() = create("com.android.tools.build", "gradle", "7.0.0-alpha15")

val DependencyHandler.`kotlin-gradle-plugin`: ExternalModuleDependency
    get() = create("org.jetbrains.kotlin", "kotlin-gradle-plugin", KotlinVersion)

val DependencyHandler.sqldelight: ExternalModuleDependency
    get() = create("com.squareup.sqldelight", "gradle-plugin", SqlDelightVersion)

val PluginDependenciesSpec.`android-application`: PluginDependencySpec
    get() = id("com.android.application")

val PluginDependenciesSpec.`android-library`: PluginDependencySpec
    get() = id("com.android.library")

val PluginDependenciesSpec.`kotlin-android`: PluginDependencySpec
    get() = kotlin("android")

val PluginDependenciesSpec.`kotlin-js`: PluginDependencySpec
    get() = id("org.jetbrains.kotlin.js")

val PluginDependenciesSpec.`kotlin-multiplatform`: PluginDependencySpec
    get() = kotlin("multiplatform")

val PluginDependenciesSpec.ktlint: PluginDependencySpec
    get() = id("org.jlleitschuh.gradle.ktlint") version "10.0.0"

val PluginDependenciesSpec.serialization: PluginDependencySpec
    get() = kotlin("plugin.serialization") version KotlinVersion

val PluginDependenciesSpec.sqldelight: PluginDependencySpec
    get() = id("com.squareup.sqldelight")

val PluginDependenciesSpec.versions: PluginDependencySpec
    get() = id ("com.github.ben-manes.versions") version "0.38.0"
