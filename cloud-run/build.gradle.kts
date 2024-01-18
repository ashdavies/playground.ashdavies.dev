plugins {
    id("com.google.cloud.tools.jib")
    id("io.ashdavies.kotlin")
    application
}

application {
    mainClass.set("io.ashdavies.cloud.MainKt")
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

                    implementation(libs.jetbrains.kotlin.test)
                    implementation(libs.jetbrains.kotlinx.coroutines.core)
                    implementation(libs.jetbrains.kotlinx.serialization.json)
                    implementation(libs.ktor.client.core)
                    implementation(libs.ktor.client.content.negotiation)
                    implementation(libs.ktor.serialization)
                    implementation(libs.ktor.serialization.json)
                    implementation(libs.ktor.server.test.host)
                    implementation(libs.ktor.http)
                    implementation(libs.ktor.utils)
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
        with(projects) {
            implementation(appCheck.appCheckCommon)
            implementation(appCheck.appCheckSdk)
            implementation(cloudFirestore)
            implementation(eventsAggregator)
            implementation(httpClient)
            implementation(localStorage)
        }

        with(libs.google) {
            implementation(dependencies.platform(cloud.bom))
            implementation(auth.http)
            implementation(cloud.firestore)
            implementation(cloud.storage)
            implementation(firebase.admin)
            implementation(guava.jre)
        }

        with(libs.jetbrains.kotlinx) {
            implementation(coroutines.core)
            implementation(datetime)
            implementation(serialization.core)
            implementation(serialization.json)
        }

        with(libs.ktor) {
            implementation(serialization.json)
            implementation(serialization.kotlinx)

            with(server) {
                implementation(auth)
                implementation(call.logging)
                implementation(cio)
                implementation(compression)
                implementation(conditional.headers)
                implementation(content.negotiation)
                implementation(core)
                implementation(default.headers)
                implementation(request.validation)
            }
        }
    }
}

tasks.withType<com.google.cloud.tools.jib.gradle.JibTask> {
    dependsOn("build")
}
