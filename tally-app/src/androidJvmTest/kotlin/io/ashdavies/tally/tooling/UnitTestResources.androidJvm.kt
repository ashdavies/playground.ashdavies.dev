package io.ashdavies.tally.tooling

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import java.io.FileInputStream

internal object UnitTestResources {

    @OptIn(ExperimentalSerializationApi::class)
    inline fun <reified T> decodeFromResource(sourceSet: String, name: String): T {
        return Json.decodeFromStream(FileInputStream(File("src/$sourceSet/resources/$name")))
    }
}
