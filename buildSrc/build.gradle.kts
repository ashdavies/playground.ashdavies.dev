plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

tasks {
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xinline-classes")
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.20-RC")
}
