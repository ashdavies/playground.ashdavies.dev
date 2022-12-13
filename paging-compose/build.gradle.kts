import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode.Disabled

plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.paging"
}

kotlin {
    commonMain.dependencies {
        api(libs.cash.paging.common)
    }

    androidMain.dependencies {
        api(libs.androidx.paging.runtime)
    }

    explicitApi = Disabled
}
