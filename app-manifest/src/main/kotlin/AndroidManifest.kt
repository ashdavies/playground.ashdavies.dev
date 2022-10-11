import java.io.File

private val Empty = """
    <?xml version="1.0" encoding="utf-8"?>
    <manifest />
""".trimIndent()

internal class AndroidManifest(private val output: File) {
    fun generate() = output.run {
        parentFile.mkdirs()
        writeText(Empty)
    }
}
