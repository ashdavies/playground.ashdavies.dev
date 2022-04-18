plugins {
    `multiplatform-library`
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.contentNegotiation)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.serialization.json)
                implementation(libs.ktor.serialization.kotlinx)
                implementation(libs.ktor.server.auth)
                implementation(libs.ktor.server.callLogging)
                implementation(libs.ktor.server.cio)
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.htmlBuilder)
                implementation(libs.qos.logbackClassic)
            }
        }
    }
}
