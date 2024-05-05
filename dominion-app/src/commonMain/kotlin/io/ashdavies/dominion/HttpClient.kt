package io.ashdavies.dominion

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.encodeURLPath
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private const val DOMINION_STRATEGY_URL = "https://wiki.dominionstrategy.com/api.php"
private const val DEFAULT_LIMIT = 500

internal suspend fun HttpClient.categoryMembers(
    cmTitle: String,
    cmType: String,
): List<String> {
    val queryString = "action=query" +
            "&list=categorymembers" +
            "&cmtitle=$cmTitle" +
            "&cmtype=$cmType" +
            "&cmlimit=$DEFAULT_LIMIT" +
            "&format=json"

    return get("$DOMINION_STRATEGY_URL?$queryString")
        .body<JsonObject>()
        .getOrThrow<JsonObject>("query")
        .getOrThrow<JsonArray>("categorymembers")
        .map { it.getContentAsString("title") }
}

internal suspend fun HttpClient.categoryImages(
    gcmTitle: String,
    regex: Regex,
): Map<String, String> {
    val queryString = "action=query" +
            "&generator=categorymembers" +
            "&gcmtitle=$gcmTitle" +
            "&gcmtype=file" +
            "&gcmlimit=$DEFAULT_LIMIT" +
            "&prop=imageinfo" +
            "&iiprop=url" +
            "&format=json"

    return imageInfo(queryString, regex)
}

internal suspend fun HttpClient.imageUrls(
    vararg titles: String,
    regex: Regex,
): Map<String, String> {
    val queryString = "action=query" +
            "&titles=${titles.joinToString("|").encodeURLPath()}" +
            "&prop=imageinfo" +
            "&iiprop=url" +
            "&format=json"

    return imageInfo(queryString, regex)
}

private suspend fun HttpClient.imageInfo(
    queryString: String,
    regex: Regex,
): Map<String, String> {
    return get("$DOMINION_STRATEGY_URL?$queryString")
        .body<JsonObject>()
        .getOrThrow<JsonObject>("query")
        .getOrThrow<JsonObject>("pages")
        .values.associate { value ->
            val key = value
                .getContentAsString("title")
                .firstGroupOrNull(regex)
                ?: error("Failed to find group for $value")

            key to value
                .getOrThrow<JsonArray>("imageinfo")
                .first()
                .getContentAsString("url")
        }
}

private fun JsonElement.getContentAsString(key: String): String =
    jsonObject.getValue(key).jsonPrimitive.content

private inline fun <reified T : JsonElement> JsonElement.getOrThrow(key: String): T =
    jsonObject.getValue(key) as? T ?: error(T::class.simpleName ?: error(T::class))
