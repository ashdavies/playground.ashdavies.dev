plugins {
    id("io.ashdavies.cloud")
    id("io.ashdavies.properties")

    alias(libs.plugins.build.config)
}

buildConfig {
    val firebaseAndroidAppId by stringPropertyOrNull { value ->
        buildConfigField<String?>("FIREBASE_ANDROID_APP_ID", value)
    }

    val gcloudProject by stringPropertyOrNull { value ->
        buildConfigField<String?>("GCLOUD_PROJECT", value)
    }

    val gcpProject by stringPropertyOrNull { value ->
        buildConfigField<String?>("GCP_PROJECT", value)
    }

    val googleCloudProject by stringPropertyOrNull { value ->
        buildConfigField<String?>("GOOGLE_CLOUD_PROJECT", value)
    }

    packageName.set("io.ashdavies.check")
}

dependencies {
    implementation(libs.auth.java.jwt)
    implementation(libs.auth.jwks.rsa)
    implementation(libs.google.auth.http)
    implementation(libs.google.firebase.admin)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.ktor.client.core)

    runtimeOnly(libs.google.guava.jre)
    runtimeOnly(libs.slf4j.simple)
}
