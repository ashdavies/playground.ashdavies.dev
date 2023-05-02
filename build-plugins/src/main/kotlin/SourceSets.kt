import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetContainer
import kotlin.properties.ReadOnlyProperty

public val KotlinMultiplatformExtension.commonMain: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.commonTest: KotlinSourceSet by SourceSetDelegate()

public val KotlinMultiplatformExtension.androidAndroidTestRelease: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.androidDebug: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.androidMain: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.androidTest: KotlinSourceSet by SourceSetDelegate()

public val KotlinMultiplatformExtension.integrationTest: KotlinSourceSet by SourceSetDelegate()

public val KotlinMultiplatformExtension.jvmMain: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.jvmTest: KotlinSourceSet by SourceSetDelegate()

public operator fun KotlinSourceSet.invoke(action: KotlinSourceSet.() -> Unit) = action()

internal typealias SourceSetDelegate = ReadOnlyProperty<KotlinSourceSetContainer, KotlinSourceSet>

internal fun SourceSetDelegate(): SourceSetDelegate = SourceSetDelegate { thisRef, property ->
    thisRef.sourceSets[property.name]
}
