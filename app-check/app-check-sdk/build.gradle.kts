plugins {
    id("io.ashdavies.cloud")
}

dependencies {
    api(libs.auth.java.jwt)
    api(libs.ktor.client.core)

    implementation(libs.auth.jwks.rsa)
    implementation(libs.bundles.ktor.client)
    implementation(libs.ktor.client.auth)
}

kotlin {
    sourceSets.all { languageSettings.optIn("kotlin.RequiresOptIn") }
    explicitApiWarning()
}
