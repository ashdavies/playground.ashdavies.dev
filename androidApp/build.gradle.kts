// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION")

import com.android.build.api.dsl.ApplicationDefaultConfig
import com.android.build.api.dsl.VariantDimension
import kotlin.properties.ReadOnlyProperty

plugins {
    id(libs.plugins.android.application)
    id(libs.plugins.cash.molecule)
    id(libs.plugins.kotlin.android)

    alias(libs.plugins.serialization)
}

android {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }

    defaultConfig {
        applicationId = "io.ashdavies.playground"
        compileSdk = 31
        minSdk = 23

        versionCode = 1
        versionName = "1.0"

        val playgroundApiKey by buildConfigField()
    }

    sourceSets.configureEach {
        java.srcDirs("src/$name/kotlin")
    }
}

fun ApplicationDefaultConfig.buildConfigField(type: String = "String"): ReadOnlyProperty<VariantDimension?, Unit> =
    CaseProperty { buildConfigField(type, it, System.getenv(it)) }

dependencies {
    implementation(project(":commonApp"))
    implementation(project(":localStorage"))

    implementation(libs.androidx.activityKtx)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.uiTooling)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.fragmentKtx)
    implementation(libs.androidx.pagingCompose)
    implementation(libs.androidx.pagingRuntime)
    implementation(libs.androidx.lifecycle.viewModelKtx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtimeKtx)
    implementation(libs.androidx.navigation.uiKtx)
    implementation(libs.google.accompanist.coil)
    implementation(libs.google.accompanist.flowlayout)
    implementation(libs.google.accompanist.insets)
    implementation(libs.google.accompanist.insetsUi)
    implementation(libs.google.accompanist.placeholderMaterial)
    implementation(libs.google.accompanist.swiperefresh)
    implementation(libs.google.accompanist.systemuicontroller)
    implementation(libs.google.firebase.analytics)
    implementation(libs.google.firebase.commonKtx)
    implementation(libs.google.android.material)
    implementation(libs.jetbrains.kotlinx.coroutinesAndroid)
    implementation(libs.jetbrains.kotlinx.coroutinesCore)
    implementation(libs.jetbrains.kotlinx.datetime)
    implementation(libs.jetbrains.kotlinx.serializationJson)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.json)
    implementation(libs.ktor.client.serialization)

    testImplementation(libs.jetbrains.kotlin.test)
    testImplementation(libs.jetbrains.kotlin.testJunit)
    testImplementation(libs.jetbrains.kotlinx.coroutinesTest)
}
