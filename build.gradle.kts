buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath(BuildPlugins.BuildTools)
        classpath(BuildPlugins.GoogleServices)
        classpath(BuildPlugins.Kotlin)
        classpath(BuildPlugins.OssLicenses)
        classpath(BuildPlugins.SafeArgs)
        classpath(BuildPlugins.Versioning)
        classpath(BuildPlugins.Versions)
    }
}
