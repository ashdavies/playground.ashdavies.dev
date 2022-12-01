import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetContainer
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

public val KotlinMultiplatformExtension.commonMain: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.commonTest: KotlinSourceSet by SourceSetDelegate()

public val KotlinMultiplatformExtension.androidDebug: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.androidMain: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.androidTest: KotlinSourceSet by SourceSetDelegate()

public val KotlinMultiplatformExtension.jvmMain: KotlinSourceSet by SourceSetDelegate()

private class SourceSetDelegate : ReadOnlyProperty<KotlinSourceSetContainer, KotlinSourceSet> {
    override fun getValue(
        thisRef: KotlinSourceSetContainer,
        property: KProperty<*>,
    ): KotlinSourceSet = thisRef.sourceSets[property.name]
}

public operator fun KotlinSourceSet.invoke(action: KotlinSourceSet.() -> Unit) = action()
