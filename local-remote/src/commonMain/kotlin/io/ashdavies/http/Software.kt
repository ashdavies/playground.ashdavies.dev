package io.ashdavies.http

internal expect object Software {
    val buildVersion: String
    val productName: String
    val clientName: String
}
