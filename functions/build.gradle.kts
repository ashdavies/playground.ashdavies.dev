import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

plugins {
    id("kotlin2js")
}

dependencies {
    testImplementation(ProjectDependencies.JetBrains.Kotlin.kotlinTestJs)
}

tasks {
    withType<Kotlin2JsCompile> {
        kotlinOptions {
            moduleKind = "commonjs"
            outputFile = "lib/index.js"
            sourceMap = true
        }
    }
}
