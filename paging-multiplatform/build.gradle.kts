import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    id("dev.ashdavies.compose")
    id("dev.ashdavies.default")
}

android {
    namespace = "dev.ashdavies.paging"
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        binaries.executable()
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.foundation)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.coroutines.core)
        }

        val androidJvmMain by getting {
            dependencies {
                implementation(libs.androidx.paging.compose)
            }
        }
    }
}
