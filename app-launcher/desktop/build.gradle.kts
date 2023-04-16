import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("io.ashdavies.kotlin")
}

kotlin {
    jvm {
        withJava()
    }

    jvmMain.dependencies {
        implementation(libs.jetbrains.kotlinx.cli)
        implementation(libs.slack.circuit.foundation)
        implementation(projects.appLauncher.common)
    }
}

compose.desktop {
    application {
        mainClass = "io.ashdavies.playground.MainKt"

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
