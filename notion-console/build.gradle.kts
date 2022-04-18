import org.jetbrains.compose.compose

plugins {
    `multiplatform-library`
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":auth-oauth"))
                implementation(project(":local-storage"))
                implementation(compose.runtime)

                implementation(libs.jetbrains.kotlinx.cli)
                implementation(libs.jetbrains.kotlinx.coroutinesCore)
                implementation(libs.jetbrains.kotlinx.serializationJson)
                implementation(libs.jraf.klibnotion)
                implementation(libs.ktor.server.core)
                implementation(libs.qos.logbackClassic)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)

                implementation(libs.ktor.server.cio)
                implementation(libs.ktor.server.core)
            }
        }
    }
}
