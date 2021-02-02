import ProjectDependencies.JetBrains
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    id("org.jetbrains.kotlin.js")
}

kotlin {
    js {
        browser {
            distribution {
                directory = File("$projectDir/lib")
            }
        }

        binaries.executable()
    }
}

dependencies {
    implementation(JetBrains.KotlinX.kotlinxDatetime)
    implementation(JetBrains.KotlinX.kotlinxNodejs)

    implementation(npm("@octokit/graphql", "4.5.8"))
    implementation(npm("@types/humps", "2.0.0"))
    implementation(npm("firebase-admin", "9.4.1"))
    implementation(npm("firebase-functions", "3.11.0"))
    implementation(npm("firestore", "1.1.6"))
    implementation(npm("yaml", "1.10.0"))
}

tasks.withType<KotlinWebpack> {
    output.libraryTarget = "commonjs2"
    outputFileName = "index.js"
}
