plugins {
    id("io.ashdavies.default")
    id("io.ashdavies.properties")
    id("io.ashdavies.sql")

    alias(libs.plugins.build.config)
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "io.ashdavies.identity"
}

buildConfig {
    val serverClientId by stringProperty { value ->
        buildConfigField("SERVER_CLIENT_ID", value)
    }

    packageName.set(android.namespace)
}

dependencies {
    val kspCommonMainMetadata by configurations.getting {
        add(name, projects.composeInject)
    }
}

kotlin {
    commonMain.dependencies {
        implementation(projects.composeAnnotations)
        implementation(projects.platformSupport)
        implementation(projects.sqlDriver)

        implementation(libs.androidx.credentials.auth)
        implementation(libs.google.android.identity)
        implementation(libs.kotlinx.coroutines.core)
    }

    commonMain.kotlin {
        srcDir("build/generated/ksp/metadata/commonMain/kotlin")
    }
}

sqldelight {
    databases.create("PlaygroundDatabase") {
        packageName.set(android.namespace)
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().all {
    if (name != "kspCommonMainKotlinMetadata") dependsOn("kspCommonMainKotlinMetadata")
}
