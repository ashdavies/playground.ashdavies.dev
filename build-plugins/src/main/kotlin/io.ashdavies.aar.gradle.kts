import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

val artifactType: Attribute<String> = Attribute.of("artifactType", String::class.java)

class AarCompatibility : AttributeCompatibilityRule<LibraryElements> {
    override fun execute(details: CompatibilityCheckDetails<LibraryElements>) {
        if (details.producerValue?.name == "aar") details.compatible()
    }
}

configurations.all {
    if (isCanBeResolved && !isCanBeConsumed && isJvm()) {
        attributes.attribute(artifactType, "jar")
    }
}

dependencies {
    attributesSchema {
        val matchingStrategy = getMatchingStrategy(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE)
        matchingStrategy.compatibilityRules.add(AarCompatibility::class.java)
    }

    registerTransform(UnpackAar::class.java) {
        from.attribute(artifactType, "aar")
        to.attribute(artifactType, "jar")
    }
}

fun Configuration.isJvm(): Boolean {
    return name.contains("jvm", ignoreCase = true)
}

abstract class UnpackAar : TransformAction<TransformParameters.None> {

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
