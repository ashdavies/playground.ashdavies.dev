import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("com.android.application")

    id("io.ashdavies.android")
    id("io.ashdavies.compose")
    id("io.ashdavies.kotlin")
    id("io.ashdavies.parcelable")
    id("io.ashdavies.properties")

    alias(libs.plugins.build.config)
}

android {
    val androidTest by sourceSets.getting {
        res.srcDirs("src/androidMain/res")
    }

    defaultConfig {
        val androidApiKey by stringProperty { _, value ->
            manifestPlaceholders["ANDROID_API_KEY"] = "$value"
        }
    }

    namespace = "io.ashdavies.routes"
}

buildConfig {
    val androidApiKey by stringProperty(::buildConfigField)

    packageName.set(android.namespace)
}

compose.desktop {
    application {
        // https://github.com/Kotlin/kotlinx.coroutines/issues/3914
        jvmArgs("-Dkotlinx.coroutines.fast.service.loader=false")

        mainClass = "io.ashdavies.routes.MapRoutesApp_jvmKt"

        nativeDistributions {
            targetFormats(TargetFormat.Deb, TargetFormat.Dmg, TargetFormat.Msi)

            packageName = "MapRoutesApp"
            packageVersion = "1.0.0"

            windows {
                upgradeUuid = "EFED2B83-8786-4096-B5B7-0366F8B691F5"
                menu = true
            }
        }
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.httpClient)
            implementation(projects.httpCommon)
            implementation(projects.kotlinDelegates)
            implementation(projects.mapsRouting)
            implementation(projects.platformScaffold)
            implementation(projects.platformSupport)

            implementation(compose.material3)

            implementation(libs.androidx.annotation)
            implementation(libs.ktor.client.core)
            implementation(libs.slack.circuit.foundation)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.google.accompanist.permissions)
            implementation(libs.google.android.location)
            implementation(libs.google.android.material)
            implementation(libs.google.maps.android.compose)
            implementation(libs.google.maps.android.utils)
            implementation(libs.kotlinx.coroutines.play.services)
            implementation(libs.slack.circuit.overlay)
            implementation(libs.squareup.okio)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(projects.keyNavigation)

            runtimeOnly(libs.kotlinx.coroutines.swing)
            runtimeOnly(libs.slf4j.simple)
        }
    }
}
