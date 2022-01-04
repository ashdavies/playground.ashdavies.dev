// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

import com.android.build.api.dsl.ApplicationDefaultConfig
import com.android.build.api.dsl.VariantDimension
import kotlin.properties.ReadOnlyProperty

plugins {
    id("playground-android-application")

    id(libs.plugins.cash.molecule)
    id(libs.plugins.kotlin.android)
    alias(libs.plugins.serialization)
}

android {
    defaultConfig {
        applicationId = "io.ashdavies.playground"

        versionCode = 1
        versionName = "1.0"

        val playgroundApiKey by buildConfigField()
    }
}

fun ApplicationDefaultConfig.buildConfigField(type: String = "String"): ReadOnlyProperty<VariantDimension?, Unit> {
    return CaseProperty { buildConfigField(type, it, System.getenv(it)) }
}

dependencies {
    implementation(project(":commonApp"))
    implementation(project(":localStorage"))

    implementation(libs.androidx.activityKtx)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.fragmentKtx)
    implementation(libs.androidx.pagingCompose)
    implementation(libs.androidx.pagingRuntime)
    implementation(libs.androidx.lifecycle.viewModelKtx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtimeKtx)
    implementation(libs.androidx.navigation.uiKtx)
    implementation(libs.google.firebase.analytics)
    implementation(libs.google.firebase.commonKtx)
    implementation(libs.google.android.material)
    implementation(libs.jetbrains.kotlinx.datetime)
    implementation(libs.jetbrains.kotlinx.serializationJson)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.json)
    implementation(libs.ktor.client.serialization)
}
