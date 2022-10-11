@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.AndroidSourceSet
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.tasks.GenerateBuildConfig
import com.android.build.gradle.tasks.ManifestProcessorTask
import com.android.build.gradle.tasks.MergeResources
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.withType

private val Project.isSyncing: Boolean
    get() = hasProperty("android.injected.invoked.from.ide")

internal class AndroidManifestPlugin : Plugin<Project> {
    override fun apply(target: Project) = target.run {
        extensions.getByType(AndroidComponentsExtension::class).finalizeDsl { android ->
            android.namespace = "${target.group}.${ModuleNameService.Default()}"
        }

        extensions.configure<LibraryExtension>("android") {
            sourceSets.getByName("main") {
                if (!manifest.srcFile.isFile) configureGeneratedManifest(target, this)
            }
        }
    }

    private fun configureGeneratedManifest(project: Project, sourceSet: AndroidSourceSet) = project.run {
        val generateAndroidManifest by tasks.registering(GenerateManifestTask::class) { }
        val manifestFile = ManifestFileProducer(project)

        if (isSyncing && !manifestFile.exists()) afterEvaluate {
            AndroidManifest(manifestFile).generate()
        }

        sourceSet.manifest {
            srcFile(manifestFile)
        }

        tasks.configureEach<GenerateBuildConfig> { mustRunAfter(generateAndroidManifest) }
        tasks.configureEach<ManifestProcessorTask> { dependsOn(generateAndroidManifest) }
        tasks.configureEach<MergeResources> { dependsOn(generateAndroidManifest) }
    }
}

private inline fun <reified T : Task> TaskContainer.configureEach(noinline configure: T.() -> Unit) {
    withType<T>().configureEach(configure)
}
