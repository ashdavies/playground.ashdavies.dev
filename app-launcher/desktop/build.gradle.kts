import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.kotlin")
}

kotlin {
    jvm {
        withJava()
    }

    commonMain.dependencies {
        implementation(projects.appLauncher.common)
        implementation(projects.httpClient)
        implementation(projects.platformSupport)

        implementation(libs.kotlinx.cli)
        implementation(libs.ktor.client.core)
        implementation(libs.slack.circuit.foundation)
    }
}

compose.desktop {
    application {
        // https://github.com/Kotlin/kotlinx.coroutines/issues/3914
        jvmArgs("-Dkotlinx.coroutines.fast.service.loader=false")

        mainClass = "io.ashdavies.playground.LauncherMainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Deb, TargetFormat.Dmg, TargetFormat.Msi)

            packageName = "LauncherApp"
            packageVersion = "1.0.0"

            windows {
                upgradeUuid = "EFED2B83-8786-4096-B5B7-0366F8B691F5"
                menu = true
            }
        }
    }
}
