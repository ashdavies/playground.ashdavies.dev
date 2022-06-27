package io.ashdavies.http

internal val softwareVersion: SoftwareVersion
    get() = SoftwareVersion()

internal sealed interface SoftwareVersion {
    val buildVersion: String
    val productName: String
}

internal data class AndroidSoftware(
    override val buildVersion: String,
    override val productName: String,
) : SoftwareVersion

internal data class WindowsSoftware(
    override val buildVersion: String,
    override val productName: String,
) : SoftwareVersion

internal data class MacSoftware(
    override val buildVersion: String,
    override val productName: String,
) : SoftwareVersion

internal fun SoftwareVersion(name: String = System.properties["os.name"] as String): SoftwareVersion = when {
    name.contains("android") -> AndroidSoftware("", "Build.FINGERPRINT")
    name.contains("win") -> WindowsSoftware("", "")
    name.contains("mac") -> MacSoftware("", "")
    else -> throw IllegalStateException("Unrecognised operating system name `$name`")
}
