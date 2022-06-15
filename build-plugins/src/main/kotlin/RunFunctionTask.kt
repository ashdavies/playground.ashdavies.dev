import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.named

private val Project.sourceSets: SourceSetContainer
    get() = extensions.getByName("sourceSets") as SourceSetContainer

private val SourceSetContainer.main: NamedDomainObjectProvider<SourceSet>
    get() = named<SourceSet>("main")

private val NamedDomainObjectContainer<Configuration>.runtimeClasspath: NamedDomainObjectProvider<Configuration>
    get() = named<Configuration>("runtimeClasspath")

@Suppress("extension_shadowed_by_member")
inline fun <reified T : Any> NamedDomainObjectCollection<out Any>.named(name: String): NamedDomainObjectProvider<T> =
    named(name, T::class)

/**
 * TODO Combine curl in task request and close server
 * curl -sSL -D - http://localhost:8080/ -o /dev/null
 */
public abstract class RunFunctionTask : JavaExec() {

    @Input lateinit var target: String
    @Input var port: Int = 8080

    init {
        with(project) {
            mainClass.set("com.google.cloud.functions.invoker.runner.Invoker")
            classpath(configurations.getByName("invoker"))
            dependsOn(tasks.named("compileKotlin"))
            group = "run"

            sourceSets.main.configure {
                inputs.files(configurations.runtimeClasspath, output)
            }
        }
    }

    override fun exec() {
        args("--target", target)
        args("--port", port)
        super.exec()
    }
}
