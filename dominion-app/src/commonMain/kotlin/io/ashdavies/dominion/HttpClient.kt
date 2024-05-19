package io.ashdavies.dominion

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.encodeURLQueryComponent
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

    return get("$DOMINION_STRATEGY_URL?${queryString.encodeURLQueryComponent()}")
        .body<JsonObject>()
        .getOrThrow<JsonObject>("query")
        .getOrThrow<JsonArray>("categorymembers")
        .map { it.getContentAsString("title") }
}

internal suspend fun HttpClient.categoryImages(
    gcmTitle: String,
    gcmLimit: Int = DEFAULT_LIMIT,
    gcmOffset: Int = 0,
    regex: Regex,
): Map<String, ImageInfo> {
    val queryString = "action=query" +
        "&generator=categorymembers" +
        "&gcmtitle=$gcmTitle" +
        "&gcmtype=file" +
        "&gcmlimit=$gcmLimit" +
        "&gcmoffset=$gcmOffset" +
        "&prop=imageinfo" +
        "&iiprop=url|size" +
        "&format=json"

    return get("$DOMINION_STRATEGY_URL?${queryString.encodeURLQueryComponent()}")
        .body<JsonObject>()
        .getOrThrow<JsonObject>("query")
        .getOrThrow<JsonObject>("pages")
        .values.associate { value ->
            val title = value
                .getContentAsString("title")
                .firstGroup(regex)

            val imageInfo = value
                .getOrThrow<JsonArray>("imageinfo")
                .first()

            title to ImageInfo(
                title = title,
                url = imageInfo.getContentAsString("url"),
                size = ImageInfo.Size(
                    width = imageInfo
                        .getContentAsString("width")
                        .toInt(),
                    height = imageInfo
                        .getContentAsString("height")
                        .toInt(),
                ),
            )
        }
}

internal data class ImageInfo(
    val title: String,
    val url: String,
    val size: Size,
) {
    data class Size(
        val width: Int,
        val height: Int,
    )
}

private fun JsonElement.getContentAsString(key: String): String =
    jsonObject.getValue(key).jsonPrimitive.content

private inline fun <reified T : JsonElement> JsonElement.getOrThrow(key: String): T =
    jsonObject.getValue(key) as? T ?: error(T::class.simpleName ?: error(T::class))
