import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

/*pluginManager.withPlugin("android") {
    plugins { id("kotlin-parcelize") }
}*/

plugins {
    kotlin("multiplatform")
}

kotlin {
    android()

    androidMain.dependencies {
        implementation(libs.androidx.annotation)
        implementation(libs.androidx.core.ktx)
        implementation(libs.google.android.material)
        implementation(libs.jetbrains.kotlinx.coroutines.android)
    }

    val androidTest by sourceSets.getting {
        val androidAndroidTestDebug by sourceSets.getting
        dependsOn(androidAndroidTestDebug)
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
        compileSdk = Playground.compileSdk
        minSdk = Playground.minSdk
    }

    sourceSets.configureEach {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}
