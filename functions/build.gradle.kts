import ProjectDependencies.JetBrains
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile

plugins {
    id("org.jetbrains.kotlin.js")
}

kotlin {
    js {
        nodejs {
            binaries.executable()
            useCommonJs()
        }
    }
}

dependencies {
    implementation(JetBrains.KotlinX.kotlinxDatetime)
    implementation(JetBrains.KotlinX.kotlinxNodejs)

    testImplementation(JetBrains.Kotlin.kotlinTestJs)
}

tasks.withType<KotlinJsCompile> {
    doLast {
        val output = File("$rootDir/build/js/packages/${rootProject.name}-${project.name}/kotlin")
        if (!output.exists()) throw IllegalStateException("Could not find output directory $output")

        copy {
            from(output)
            into("$projectDir/lib")
        }
    }
}
