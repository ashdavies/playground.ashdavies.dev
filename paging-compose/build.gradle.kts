@file:Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

val Configuration.isJvm: Boolean
    get() = name.contains("jvm", ignoreCase = true)

val artifactType: Attribute<String> =
    Attribute.of("artifactType", String::class.java)

plugins {
    alias(libs.plugins.johnrengelman.shadow)
    id("io.ashdavies.library")
}

android {
    namespace = "io.ashdavies.paging"
}

configurations.all {
    if (isCanBeResolved && !isCanBeConsumed && isJvm) {
        attributes.attribute(artifactType, "jar")
    }
}

dependencies {
    attributesSchema {
        getMatchingStrategy(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE)
            .compatibilityRules
            .add(AarCompatibility::class.java)
    }

    registerTransform(UnpackAar::class.java) {
        from.attribute(artifactType, "aar")
        to.attribute(artifactType, "jar")
    }
}

kotlin {
    val commonMain by sourceSets.dependencies {
        api(libs.cash.paging.common)
    }

    val androidMain by sourceSets.dependencies {
        api(libs.bundles.androidx.paging)
    }

    val jvmMain by sourceSets.dependencies {
        api(libs.androidx.paging.compose)
    }
}

val repackageJar by tasks.creating(ShadowJar::class.java) {
    dependencies { include(dependency(libs.androidx.paging.compose.get())) }
    configurations = listOf(project.configurations["jvmRuntimeClasspath"])
    from(kotlin.jvm().compilations["main"].output)
}

public class AarCompatibility : AttributeCompatibilityRule<LibraryElements> {
    override fun execute(details: CompatibilityCheckDetails<LibraryElements>) {
        if (details.producerValue?.name == "aar") details.compatible()
    }
}

public abstract class UnpackAar : TransformAction<TransformParameters.None> {

    @get:InputArtifact
    abstract val inputArtifact: Provider<FileSystemLocation>

    override fun transform(outputs: TransformOutputs) {
        ZipFile(inputArtifact.get().asFile).use { zip ->
            zip.entries().asSequence()
                .filter { !it.isDirectory }
                .filter { it.name.endsWith(".jar") }
                .forEach { zip.unzip(it, outputs.file(it.name)) }
        }
    }

    private fun ZipFile.unzip(entry: ZipEntry, output: File) {
        getInputStream(entry).use {
            Files.copy(it, output.toPath())
        }
    }
}
