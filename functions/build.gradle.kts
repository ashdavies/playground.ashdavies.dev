import ProjectDependencies.JetBrains

plugins {
    id("org.jetbrains.kotlin.js")
}

kotlin {
    js {
        nodejs()
    }
}

dependencies {
    implementation(JetBrains.KotlinX.kotlinxDatetime)
    implementation(JetBrains.KotlinX.kotlinxNodejs)

    testImplementation(JetBrains.Kotlin.kotlinTestJs)
}
