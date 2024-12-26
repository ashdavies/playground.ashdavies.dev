import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.default")
}

android {
    namespace = "androidx.paging.compose"
}

kotlin {
    explicitApi = ExplicitApiMode.Disabled

    sourceSets.commonMain.dependencies {
        implementation(libs.androidx.paging.common)
        implementation(compose.foundation)
    }
}
