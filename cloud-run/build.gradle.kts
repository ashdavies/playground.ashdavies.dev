plugins {
    id("com.google.cloud.tools.jib")
    id("io.ashdavies.kotlin")
    id("io.ashdavies.properties")

    application

    alias(libs.plugins.build.config)
}

application {
    mainClass.set("io.ashdavies.cloud.MainKt")
}

buildConfig {
    val firebaseAndroidAppId by stringPropertyOrNull { value ->
        buildConfigField<String?>("FIREBASE_ANDROID_APP_ID", value)
    }

    val googleServiceAccountId by stringPropertyOrNull { value ->
        buildConfigField<String?>("GOOGLE_SERVICE_ACCOUNT_ID", value)
    }

    val integrationApiKey by stringProperty { value ->
        buildConfigField("INTEGRATION_API_KEY", value)
    }

    packageName.set("io.ashdavies.cloud")
}

jib {
    container.mainClass = application.mainClass.get()
}

kotlin {
    explicitApiWarning()

    jvm {
        compilations {
            val main by compilations.getting
            val test by compilations.getting

            val integrationTest by compilations.creating {
                defaultSourceSet.dependencies {
                    implementation(main.compileDependencyFiles + main.output.classesDirs)
                }

                tasks.register<Test>("integrationTest") {
                    classpath = compileDependencyFiles + runtimeDependencyFiles + output.allOutputs
                    description = "Run integration tests."
                    group = LifecycleBasePlugin.VERIFICATION_GROUP
                    testClassesDirs = output.classesDirs
                    testLogging { events("passed") }
                }
            }

            integrationTest.associateWith(test)
        }

        withJava()
    }

    commonMain.dependencies {
        implementation(projects.appCheck.appCheckCommon)
        implementation(projects.appCheck.appCheckSdk)
        implementation(projects.cloudFirestore)
        implementation(projects.eventsAggregator)
        implementation(projects.httpClient)
        implementation(projects.httpCommon)
        implementation(projects.localStorage)

        implementation(libs.google.firebase.admin)
        implementation(libs.kotlinx.datetime)
        implementation(libs.kotlinx.serialization.core)
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.serialization.kotlinx.json)
        implementation(libs.ktor.server.call.logging)
        implementation(libs.ktor.server.cio)
        implementation(libs.ktor.server.compression)
        implementation(libs.ktor.server.conditional.headers)
        implementation(libs.ktor.server.content.negotiation)
        implementation(libs.ktor.server.core)
        implementation(libs.ktor.server.default.headers)
        implementation(libs.ktor.server.host.common)
    }

    commonTest.dependencies {
        implementation(kotlin("test"))

        implementation(libs.ktor.server.test.host)
    }

    jvmIntegrationTest.dependencies {
        implementation(libs.app.cash.turbine)
        implementation(libs.kotlinx.coroutines.test)
        implementation(libs.ktor.client.content.negotiation)
    }
}

tasks.withType<com.google.cloud.tools.jib.gradle.JibTask> {
    dependsOn("build")
}
