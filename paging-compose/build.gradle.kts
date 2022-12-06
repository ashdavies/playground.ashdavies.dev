plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.aar")
}

android {
    namespace = "io.ashdavies.paging"
}

kotlin {
    commonMain.dependencies {
        api(libs.cash.paging.common)
    }

    androidMain.dependencies {
        api(libs.bundles.androidx.paging)
    }

    jvmMain.dependencies {
        implementation(libs.androidx.paging.compose) {
            exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-android")
        }
    }
}

val jvmJar by tasks.getting(Jar::class) {
    doFirst { from(dependency(libs.androidx.paging.compose)) }
}
