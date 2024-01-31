import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation

plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.kotlin")
    id("io.ashdavies.sql")

    application
}

application {
    mainClass.set("io.ashdavies.notion.MainKt")
}

kotlin {
    commonMain.dependencies {
        implementation(projects.composeCli)
        implementation(projects.localStorage)

        implementation(compose.runtime)
        implementation(compose.ui)

        implementation(libs.jraf.klibnotion)
        implementation(libs.kotlinx.cli)
        implementation(libs.kotlinx.serialization.core)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.ktor.client.auth)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.logging)
        implementation(libs.ktor.http)
        implementation(libs.ktor.serialization.kotlinx.json)
        implementation(libs.ktor.server.auth)
        implementation(libs.ktor.server.cio)
        implementation(libs.ktor.server.core)
        implementation(libs.ktor.server.host.common)
        implementation(libs.mosaic.runtime)
    }

    tasks.withType<JavaExec> {
        val compilation: KotlinJvmCompilation = jvm()
            .compilations
            .getByName("main")

        val classes: ConfigurableFileCollection = files(
            compilation.runtimeDependencyFiles,
            compilation.output.allOutputs
        )

        classpath(classes)
    }
}

configurations.all {
    resolutionStrategy {
        force(libs.fusesource.jansi)
    }
}

sqldelight {
    databases {
        create("PlaygroundDatabase") {
            packageName.set("io.ashdavies.notion")
            dependency(projects.localStorage)
        }
    }
}
