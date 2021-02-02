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
}

tasks.withType<KotlinWebpack> {
    output.libraryTarget = "commonjs2"
    outputFileName = "index.js"
}
