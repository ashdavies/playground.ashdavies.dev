import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetContainer
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

public val KotlinMultiplatformExtension.commonMain: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.commonTest: KotlinSourceSet by SourceSetDelegate()

public val KotlinMultiplatformExtension.androidAndroidTestRelease: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.androidDebug: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.androidMain: KotlinSourceSet by SourceSetDelegate()
public val KotlinMultiplatformExtension.androidTest: KotlinSourceSet by SourceSetDelegate()

public val KotlinMultiplatformExtension.jvmMain: KotlinSourceSet by SourceSetDelegate()

/*public fun KotlinDependencyHandler.platform(provider: Provider<MinimalExternalModuleDependency>): Dependency {
    return project.dependencies.platform("${provider.get()}")
}*/

private class SourceSetDelegate : ReadOnlyProperty<KotlinSourceSetContainer, KotlinSourceSet> {
    override fun getValue(thisRef: KotlinSourceSetContainer, property: KProperty<*>): KotlinSourceSet {
        return thisRef.sourceSets[property.name]
    }
}

public operator fun KotlinSourceSet.invoke(action: KotlinSourceSet.() -> Unit) = action()
