package io.ashdavies.http

internal actual object Software {
    actual val buildVersion: String = Environment.properties["os.version"] as String
    actual val productName: String = Environment.properties["os.name"] as String
    actual val clientName: String = "Ktor/2.0.0"
    actual val gitCommit: String = Runtime
        .getRuntime()
        .exec("git rev-parse HEAD")
        .readText()
        .substring(0, 8)
}

private fun Process.readText() = inputStream
    .reader()
    .readText()
