import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.compose")

    kotlin("plugin.serialization")
    kotlin("jvm")
}

compose {
    val composeCompilerVersion = libs.versions.compose.compiler.get()
    kotlinCompilerPlugin.set("$composeCompilerVersion")
}

dependencies {
    implementation(compose.foundation)
    implementation(compose.runtime)

    with(libs.jetbrains.kotlinx) {
        implementation(coroutines.core)
        implementation(datetime)
        implementation(serialization.core)
        implementation(serialization.json)
    }

    implementation(libs.google.firebase.admin)

    testImplementation(kotlin("test"))
    testImplementation(libs.jetbrains.kotlinx.coroutines.test)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += Playground.freeCompilerArgs
    kotlinOptions.jvmTarget = "${Playground.jvmTarget}"
}
