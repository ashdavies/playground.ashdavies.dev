plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

tasks {
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + "-Xinline-classes"
            jvmTarget = "1.8"
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.0")
}
