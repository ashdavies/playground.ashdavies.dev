import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

pluginManager.withPlugin("android") {
    plugins { id("kotlin-parcelize") }
}

plugins {
    id("org.jetbrains.compose")

    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    explicitApiWarning()
    android()
    jvm()

    sourceSets.all {
        languageSettings.optIn("kotlin.RequiresOptIn")
    }

    val compose = ComposePlugin.Dependencies

    @Suppress("UNUSED_VARIABLE")
    @OptIn(ExperimentalComposeLibrary::class)
    val commonMain by sourceSets.dependencies {
        implementation(compose.foundation)
        implementation(compose.material3)
        implementation(compose.runtime)
        implementation(compose.uiTooling)
        implementation(compose.ui)

        implementation(libs.bundles.arkivanov.decompose)
        implementation(libs.bundles.jetbrains.kotlinx)
        implementation(libs.oolong)
    }

    @Suppress("UNUSED_VARIABLE")
    val commonTest by sourceSets.dependencies {
        implementation(libs.bundles.jetbrains.kotlin.test)
    }

    @Suppress("UNUSED_VARIABLE")
    val androidMain by sourceSets.dependencies {
        implementation(libs.androidx.annotation)
        implementation(libs.androidx.core.ktx)
        implementation(libs.google.android.material)
        implementation(libs.jetbrains.kotlinx.coroutines.android)
    }

    @Suppress("UNUSED_VARIABLE")
    val androidTest by sourceSets.getting {
        val androidAndroidTestDebug by sourceSets.getting
        dependsOn(androidAndroidTestDebug)
    }

    @Suppress("UNUSED_VARIABLE")
    val jvmMain by sourceSets.dependencies {
        implementation(compose.desktop.currentOs)
        implementation(libs.jetbrains.kotlinx.coroutines.swing)
    }
}

extensions.withExtension<BaseAppModuleExtension> { configure() }

extensions.withExtension<LibraryExtension> { configure() }

fun CommonExtension<*, *, *, *>.configure() {
    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    defaultConfig {
        compileSdk = 33
        minSdk = 21
    }

    sourceSets.configureEach {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += Playground.freeCompilerArgs
    kotlinOptions.jvmTarget = Playground.jvmTarget
}
