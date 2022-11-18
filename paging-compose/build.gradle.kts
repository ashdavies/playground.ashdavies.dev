import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("io.ashdavies.library")
    id("io.ashdavies.aar")
}

android {
    namespace = "io.ashdavies.paging"
}

kotlin {
    val commonMain by sourceSets.dependencies {
        api(libs.cash.paging.common)
    }

    val androidMain by sourceSets.dependencies {
        api(libs.bundles.androidx.paging)
    }

    val jvmMain by sourceSets.dependencies {
        api(files("${project.buildDir}/intermediates/aar_unpack/jvmMain/classes"))
        api(libs.androidx.paging.compose)
    }
}

val unpackAar by tasks.registering(Copy::class) {
    fun Configuration.isJvm(): Boolean = name.contains("jvm", ignoreCase = true)
    fun Dependency.artifact(): String = "$name-$version"

    val destination = "${project.buildDir}/intermediates/aar_unpack"
    val kotlin = project.extensions.getByName("kotlin") as KotlinMultiplatformExtension
    val runtimeClasspath = configurations["jvmRuntimeClasspath"]
    val sourceSets = kotlin.sourceSets

    val targets = configurations.filter { it.isJvm() && it.dependencies.isNotEmpty() }
    val input = targets.associate { it.name to it.dependencies.map(Dependency::artifact) }

    for (target in targets) {
        val sourceSet = sourceSets.first { target.name.contains(it.name, ignoreCase = true) }
        val artifacts = target.dependencies.map(Dependency::artifact)

        for (artifact in artifacts) {
            val aar = runtimeClasspath.find { it.name.endsWith("$artifact.aar") } ?: continue
            val files = zipTree(aar).matching { include("*.jar") }

            for (file in files) copy {
                val dir = "$destination/${sourceSet.name}/${file.nameWithoutExtension}"
                into("$destination/${sourceSet.name}/${file.nameWithoutExtension}")
                // target.dependencies.add(project.dependencies.create(files(dir)))
                from(zipTree(file))
            }
        }

        inputs.properties(input)
        outputs.dir(destination)
    }
}

val preBuild: Task by tasks.getting {
    dependsOn(unpackAar)
}
