import ProjectDependencies.AnvilVersion
import ProjectDependencies.KotlinVersion
import ProjectDependencies.SqlDelightVersion
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.create

val DependencyHandler.apollo: ExternalModuleDependency
    get() = create("com.apollographql.apollo", "apollo-gradle-plugin", "2.5.9")

val DependencyHandler.`batik-ext`: ExternalModuleDependency
    get() = create("org.apache.xmlgraphics", "batik-ext", "1.14")

val DependencyHandler.gradle: ExternalModuleDependency
    get() = create("com.android.tools.build", "gradle", "7.1.0-alpha06")

val DependencyHandler.`kotlin-gradle-plugin`: ExternalModuleDependency
    get() = create("org.jetbrains.kotlin", "kotlin-gradle-plugin", KotlinVersion)

val DependencyHandler.sqldelight: ExternalModuleDependency
    get() = create("com.squareup.sqldelight", "gradle-plugin", SqlDelightVersion)
