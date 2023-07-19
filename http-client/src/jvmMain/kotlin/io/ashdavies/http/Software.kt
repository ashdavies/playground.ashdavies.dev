package io.ashdavies.http

public actual object Software {
    public actual val buildVersion: String = Environment.properties["os.version"] as String
    public actual val productName: String = Environment.properties["os.name"] as String
    public actual val clientName: String = "Ktor/2.0.0"
}
