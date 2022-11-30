@file:Suppress("UNUSED_VARIABLE")

plugins {
    id("com.squareup.sqldelight")
    kotlin("multiplatform")
}

kotlin {
    @Suppress("UNUSED_VARIABLE")
    val commonMain by sourceSets.dependencies {
        with(libs.sqldelight) {
            implementation(coroutines.extensions)
            implementation(runtime)
        }
    }

    @Suppress("UNUSED_VARIABLE")
    val androidMain by sourceSets.dependencies {
        implementation(libs.sqldelight.android.driver)
    }

    @Suppress("UNUSED_VARIABLE")
    val jvmMain by sourceSets.dependencies {
        implementation(libs.sqldelight.sqlite.driver)
    }
}
