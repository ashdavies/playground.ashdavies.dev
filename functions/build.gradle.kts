import ProjectDependencies.JetBrains

plugins {
    `kotlin-js`
    serialization
}

kotlin {
    js {
        browser {
            distribution {
                directory = projectDir
            }

            testTask {
                enabled = false

                useKarma {
                    useFirefoxDeveloperHeadless()
                }
            }

            webpackTask {
                outputFileName = "index.js"
                output.libraryTarget = "commonjs2"
            }
        }

        binaries.executable()
    }
}

dependencies {
    implementation(project(":shared"))

    implementation(JetBrains.KotlinX.kotlinxCoroutinesCore)
    implementation(JetBrains.KotlinX.kotlinxDatetime)
    implementation(JetBrains.KotlinX.kotlinxNodejs)
    implementation(JetBrains.KotlinX.kotlinxSerializationCore)
    implementation(JetBrains.KotlinX.kotlinxSerializationJson)

    implementation(npm("@google-cloud/firestore", "4.12.0"))
    implementation(npm("@octokit/graphql", "4.6.2"))
    implementation(npm("firebase", "8.6.2"))
    implementation(npm("firebase-admin", "9.8.0"))
    implementation(npm("firebase-functions", "3.14.1"))

    testImplementation(kotlin("test-js"))
}