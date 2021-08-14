import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

buildscript {
    dependencies {
        classpath(`batik-ext`)
        classpath(`kotlin-gradle-plugin`)
        classpath(gradle)
        classpath(sqldelight)
        classpath(apollo)
    }
}

plugins {
    anvil
    ktlint
    versions
}

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

subprojects {
    tasks.withType<KotlinCompile<*>> {
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.FlowPreview",
            "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi",
            "-Xallow-result-return-type",
            "-XXLanguage:+InlineClasses",
            "-Xmulti-platform"
        )
    }
}
