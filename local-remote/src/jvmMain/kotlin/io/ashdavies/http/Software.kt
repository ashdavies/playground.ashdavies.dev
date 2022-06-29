package io.ashdavies.http

internal actual object Software {
    actual val buildVersion: String = Environment.properties["os.version"] as String
    actual val productName: String = Environment.properties["os.name"] as String
}
