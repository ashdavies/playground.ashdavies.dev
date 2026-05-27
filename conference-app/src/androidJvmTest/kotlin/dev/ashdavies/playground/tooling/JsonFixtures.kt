package dev.ashdavies.playground.tooling

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import java.io.FileInputStream

@OptIn(ExperimentalSerializationApi::class)
internal inline fun <reified T> Json.decodeFromResource(path: String, name: String): T {
    return decodeFromStream(FileInputStream(File("src/$path/resources/$name")))
}
