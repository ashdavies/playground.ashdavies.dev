plugins {
    id("com.android.library")
    id("io.ashdavies.android")
    id("io.ashdavies.kotlin")
    id("io.ashdavies.aar")
}

android {
    namespace = "io.ashdavies.paging"
}

kotlin {
    val commonMain by sourceSets.dependencies {
        api(libs.cash.paging.common)
    }

    val androidMain by sourceSets.dependencies {
        api(libs.bundles.androidx.paging)
    }

    val jvmMain by sourceSets.dependencies {
        implementation(libs.androidx.paging.compose)
    }
}

val jvmJar by tasks.getting(Jar::class) {
    doFirst { from(dependency(libs.androidx.paging.compose)) }
}
