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
                    implementation(kotlin("test-junit"))

                    implementation(libs.ktor.client.content.negotiation)
                    implementation(libs.ktor.serialization.json)
                    implementation(libs.ktor.server.test.host)
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

    commonMain {
        dependencies {
            with(projects) {
                implementation(appCheck.appCheckSdk)
                implementation(cloudFirestore)
                implementation(httpClient)
                implementation(eventsAggregator)
                implementation(localStorage)
            }

            with(libs.google) {
                implementation(dependencies.platform(cloud.bom))
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
}

tasks.withType<com.google.cloud.tools.jib.gradle.JibTask> {
    dependsOn("build")
}
