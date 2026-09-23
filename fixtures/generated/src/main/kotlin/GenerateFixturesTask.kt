import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.File

public abstract class GenerateFixturesTask : DefaultTask() {

    @get:InputDirectory
    @get:Optional
    @get:PathSensitive(PathSensitivity.RELATIVE)
    public abstract val inputDir: DirectoryProperty

    @get:Input
    public abstract val packageName: Property<String>

    @get:OutputDirectory
    public abstract val outputDir: DirectoryProperty

    @TaskAction
    public fun generate() {
        val inDir = inputDir.orNull?.asFile?.takeIf { it.exists() } ?: return
        val outDir = outputDir.get().asFile

        outDir.deleteRecursively()
        outDir.mkdirs()

        val jsonFiles = inDir.walkTopDown()
            .filter { it.isFile && it.extension.equals("json", ignoreCase = true) }
            .toList()
            .takeIf { it.isNotEmpty() }
            ?: return

        val packageName = packageName.get()

        jsonFiles.forEach { jsonFile ->
            val constName = jsonFile.nameWithoutExtension
                .replace(Regex("[^a-zA-Z0-9_]"), "_")
                .replaceFirstChar { it.uppercase() }
                .let { it + "Json" }

            val generatedFile = File(outDir, "${packageName.replace('.', '/')}/$constName.kt")
            val rawContent = jsonFile.readText().replace("$", "\${'$'}")

            generatedFile.parentFile.mkdirs()
            generatedFile.writeText(
                """
                |package $packageName
                |
                |public const val $constName: String = ""${'"'}$rawContent""${'"'}
                |
                """.trimMargin(),
            )
        }
    }
}
