import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

public class GeneratedFixturesPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val extension = extensions.create<GeneratedFixturesExtension>("fixtures")

        val task = tasks.register<GenerateFixturesTask>("generateFixtures") {
            inputDir.convention(extension.inputDir.orElse(provider { layout.projectDirectory.dir("fixtures") }))
            outputDir.convention(layout.buildDirectory.dir("generated/fixtures/kotlin"))

            packageName.convention(
                extension.packageName.orElse(
                    provider { "${project.group}.${project.name.replace('-', '.')}.fixtures" },
                ),
            )
        }

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain.configure {
                kotlin.srcDir(task.map { it.outputDir })
            }
        }
    }
}
