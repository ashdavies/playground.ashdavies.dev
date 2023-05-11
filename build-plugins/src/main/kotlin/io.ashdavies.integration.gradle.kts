plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {
        compilations {
            val integrationTest by creating {
                defaultSourceSet {
                    dependsOn(sourceSets.getByName("jvmMain"))
                    dependsOn(sourceSets.getByName("jvmTest"))
                }

                tasks.register<Test>("integrationTest") {
                    classpath = compileDependencyFiles + runtimeDependencyFiles + output.allOutputs
                    group = LifecycleBasePlugin.VERIFICATION_GROUP
                    description = "Runs integration tests"
                    testClassesDirs = output.classesDirs
                    outputs.upToDateWhen { false }
                }
            }
        }
    }
}
