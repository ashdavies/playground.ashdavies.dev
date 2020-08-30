object BuildPlugins {

    val BuildTools = PluginDescriptor("com.android.tools.build:gradle", "4.2.0-alpha08")
    val Detekt = PluginDescriptor("io.gitlab.arturbosch.detekt:detekt-gradle-plugin", "1.12.0")
    val GoogleServices = PluginDescriptor("com.google.gms:google-services", "4.3.3")
    val GradleDoctor = PluginDescriptor("com.osacky.doctor:doctor-plugin", "0.5.0")
    val KtLint = PluginDescriptor("org.jlleitschuh.gradle:ktlint-gradle","9.3.0")
    val Kotlin = PluginDescriptor("org.jetbrains.kotlin:kotlin-gradle-plugin", "1.4.0")
    val OssLicenses = PluginDescriptor("com.google.android.gms:oss-licenses-plugin", "0.10.2")
    val SafeArgs = PluginDescriptor("androidx.navigation:navigation-safe-args-gradle-plugin", "2.3.0")
    val Versioning = PluginDescriptor("eu.appcom.gradle:android-versioning", "1.0.2")
    val Versions = PluginDescriptor("com.github.ben-manes:gradle-versions-plugin", "0.29.0")
}
