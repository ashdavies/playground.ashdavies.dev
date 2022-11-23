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
        api(libs.androidx.paging.compose)
    }
}

val jvmJar by tasks.getting(Jar::class) {
    doFirst { from(dependency(libs.androidx.paging.compose)) }
}

// Filter all artifacts by jar value instead of name or path
fun Jar.dependency(provider: Provider<MinimalExternalModuleDependency>): List<FileTree> {
    val jvmRuntimeClasspath: FileCollection by configurations
    val dependency = provider.get()


    return jvmRuntimeClasspath.filter { file ->
        val fileTree = if (file.isDirectory) file as FileTree else zipTree(file)
        val version = fileTree.firstOrNull { it.extension == "version" } ?: return@filter false
        if (dependency.module.group in version.name) {
            return@filter true
        }
        false
    }.map { if (it.isDirectory) it as FileTree else zipTree(it) }
}
