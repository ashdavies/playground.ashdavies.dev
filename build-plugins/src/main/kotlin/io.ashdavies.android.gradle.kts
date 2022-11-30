import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.kotlin

/*pluginManager.withPlugin("android") {
    plugins { id("kotlin-parcelize") }
}*/

plugins {
    kotlin("multiplatform")
}

kotlin {
    android()

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
