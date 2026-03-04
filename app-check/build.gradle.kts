plugins {
    alias(libs.plugins.build.config)

    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.properties")
}

buildConfig {
    val firebaseAndroidAppId by stringProperty(::buildConfigField)
    val gcloudProject by stringProperty(::buildConfigField)
    val gcpProject by stringProperty(::buildConfigField)
    val googleCloudProject by stringProperty(::buildConfigField)

    packageName.set("dev.ashdavies.check")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.ktor.client.core)
        }

        jvmMain.dependencies {
            implementation(libs.auth.java.jwt)
            implementation(libs.auth.jwks.rsa)
            implementation(libs.google.auth.http)
            implementation(libs.google.firebase.admin)

            runtimeOnly(libs.google.guava.jre)
            runtimeOnly(libs.slf4j.simple)
        }
    }
}
