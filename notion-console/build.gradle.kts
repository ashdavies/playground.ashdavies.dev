import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation

plugins {
    id("io.ashdavies.kotlin")
    application
}

kotlin {
    commonMain.dependencies {
        implementation(projects.authOauth)
        implementation(projects.localStorage)

        implementation(libs.bundles.jetbrains.kotlinx)
        implementation(libs.bundles.ktor.client)
        implementation(libs.jetbrains.kotlinx.cli)
        implementation(libs.jraf.klibnotion)
        implementation(libs.qos.logbackClassic)
    }

    jvmMain.dependencies {
        implementation(libs.jakeWharton.mosaic) {
            check(group == "com.jakewharton.mosaic")
            exclude(group, "compose-runtime")
        }
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

application {
    mainClass.set("io.ashdavies.notion.MainKt")
}
