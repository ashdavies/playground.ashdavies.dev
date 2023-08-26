import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation

plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.kotlin")

    application
}

application {
    mainClass.set("io.ashdavies.notion.MainKt")
}

kotlin {
    commonMain.dependencies {
        with(projects) {
            implementation(composeCli)
            implementation(localStorage)
            implementation(notionClient)
            implementation(platformSupport)
        }

        implementation(libs.jraf.klibnotion)
        implementation(libs.ktor.serialization.json)
        implementation(libs.ktor.serialization.kotlinx)

        with(libs.ktor.server) {
            implementation(auth)
            implementation(call.logging)
            implementation(compression)
            implementation(conditional.headers)
            implementation(content.negotiation)
            implementation(core)
            implementation(default.headers)
            implementation(request.validation)
        }

        implementation(libs.mosaic.runtime)
        implementation(libs.qos.logbackClassic)
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
