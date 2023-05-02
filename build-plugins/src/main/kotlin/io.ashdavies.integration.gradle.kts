plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {
        compilations {
            val main by getting

            val integration by compilations.creating {
                defaultSourceSet.dependencies {
                    implementation(main.compileDependencyFiles + main.output.classesDirs)
                }

                tasks.register<Test>("integrationTest") {
                    classpath = compileDependencyFiles + runtimeDependencyFiles + output.allOutputs
                    testClassesDirs = output.classesDirs
                }
            }
        }
    }

    val integrationTest by sourceSets.creating
}
