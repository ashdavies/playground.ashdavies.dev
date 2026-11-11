import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

public val NamedDomainObjectContainer<KotlinSourceSet>.jvmIntegrationTest: NamedDomainObjectProvider<KotlinSourceSet>
    get() = named("jvmIntegrationTest")

internal class IntegrationTestPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        plugins.apply(libs.plugins.kotlin.multiplatform)

        extensions.configure<KotlinMultiplatformExtension> {
            val commonTest = sourceSets.commonTest.get()

            jvm {
                val jvmMain = compilations.getByName("main")

                val integrationTest = compilations.create("integrationTest") {
                    defaultSourceSet.dependsOn(commonTest)
                    associateWith(jvmMain)
                }

                testRuns.create("integration") {
                    setExecutionSourceFrom(integrationTest)
                }
            }

            sourceSets.commonMain
        }
    }
}
