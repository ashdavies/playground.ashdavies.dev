import ProjectDependencies.JetBrains

plugins {
    id("org.jetbrains.kotlin.js")
}

kotlin {
    js {
        nodejs()

        binaries.executable()
    }
}

dependencies {
    implementation(JetBrains.KotlinX.kotlinxCoroutinesCore)
    implementation(JetBrains.KotlinX.kotlinxDatetime)
    implementation(JetBrains.KotlinX.kotlinxNodejs)

    implementation(npm("@octokit/graphql", "4.5.8"))
    implementation(npm("firebase", "8.2.5"))
    implementation(npm("firebase-admin", "9.4.1"))
    implementation(npm("firebase-functions", "3.11.0"))
    implementation(npm("firestore", "1.1.6"))
    implementation(npm("humps", "2.0.1"))
    implementation(npm("yaml", "1.10.0"))
}
