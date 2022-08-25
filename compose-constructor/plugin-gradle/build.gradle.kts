import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-gradle-plugin")
    kotlin("jvm")
}

dependencies {
    implementation(libs.jetbrains.kotlin.gradle.plugin.api)
}

gradlePlugin {
    plugins {
        create("compose-constructor-plugin") {
            implementationClass = "io.ashdavies.playground.gradle.ComposeConstructorSubplugin"
            displayName = "Compose Remember Constructor Compiler Plugin"
            description = displayName
            id = name
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
