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
        implementation(projects.authOauth)
        implementation(projects.composeLocals)
        implementation(projects.localStorage)
        implementation(projects.sqlDriver)

        implementation(libs.bundles.jetbrains.kotlinx)
        implementation(libs.bundles.ktor.client)

        implementation(libs.jakeWharton.mosaic.runtime)
        implementation(libs.jetbrains.kotlinx.cli)
        implementation(libs.jraf.klibnotion)
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

sqldelight {
    database("PlaygroundDatabase") {
        dependency(projects.localStorage.dependencyProject)
        packageName = "io.ashdavies.notion"
    }
}
