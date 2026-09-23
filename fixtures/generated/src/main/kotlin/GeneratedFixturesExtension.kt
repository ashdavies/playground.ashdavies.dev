import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property

public interface GeneratedFixturesExtension {
    public val inputDir: DirectoryProperty
    public val packageName: Property<String>
}
