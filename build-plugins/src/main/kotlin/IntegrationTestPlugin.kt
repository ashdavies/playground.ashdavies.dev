import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

public val NamedDomainObjectContainer<KotlinSourceSet>.jvmIntegrationTest: NamedDomainObjectProvider<KotlinSourceSet>
    get() = named("jvmIntegrationTest")

internal class IntegrationTestPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        plugins.apply(libs.plugins.kotlin.multiplatform)

        extensions.configure<KotlinMultiplatformExtension> {
            jvm {
                val main = compilations.getByName("main")

                val integrationTest = compilations.create("integrationTest") {
                    defaultSourceSet.dependencies { implementation(kotlin("test-junit")) }
                    associateWith(main)
                }

                tasks.register<Test>("integrationTest") {
                    description = "Runs the integration tests for the JVM target."
                    group = LifecycleBasePlugin.VERIFICATION_GROUP
                    testLogging { events("passed") }

                    with(integrationTest) {
                        classpath = runtimeDependencyFiles + output.allOutputs
                        testClassesDirs = output.classesDirs
                    }
                }
            }
        }
    }
}
