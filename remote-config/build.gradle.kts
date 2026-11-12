plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")
}

kotlin {
    android.namespace = "dev.ashdavies.config"

    sourceSets {
        commonMain.dependencies {
            api(libs.ktor.client.core)

            implementation(projects.metroExtensions)

            implementation(libs.kotlinx.serialization.core)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            api(dependencies.platform(libs.google.firebase.bom))
            api(libs.google.firebase.config)
        }
    }
}
