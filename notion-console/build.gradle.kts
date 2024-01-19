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
        implementation(projects.composeCli)
        implementation(projects.localStorage)
        implementation(projects.notionClient)

        implementation(compose.runtime)
        implementation(compose.ui)

        implementation(libs.jraf.klibnotion)
        implementation(libs.kotlinx.cli)
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
