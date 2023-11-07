import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetContainer
import kotlin.properties.ReadOnlyProperty

public val KotlinMultiplatformExtension.androidMain: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.androidDebug: SourceSetInvoker by InvokerDelegate()

public val KotlinMultiplatformExtension.commonMain: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.commonTest: KotlinSourceSet by SourceSetDelegate()

public val KotlinMultiplatformExtension.jvmIntegrationTest: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.jvmMain: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.jvmTest: KotlinSourceSet by SourceSetDelegate()

public operator fun KotlinSourceSet.invoke(action: KotlinSourceSet.() -> Unit) = action()

internal typealias SourceSetDelegate = ReadOnlyProperty<KotlinSourceSetContainer, KotlinSourceSet>
internal typealias SourceInvokerDelegate = ReadOnlyProperty<KotlinMultiplatformExtension, SourceSetInvoker>
internal typealias SourceSetInvoker = (KotlinSourceSet.() -> Unit) -> Unit

internal fun SourceSetDelegate(): SourceSetDelegate = SourceSetDelegate { thisRef, property ->
    thisRef.sourceSets[property.name]
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
internal fun InvokerDelegate(): SourceInvokerDelegate = SourceInvokerDelegate { thisRef, property ->
    { thisRef.apply { sourceSets.invokeWhenCreated(property.name, it) } }
}
