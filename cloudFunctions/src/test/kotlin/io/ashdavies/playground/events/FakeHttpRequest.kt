package io.ashdavies.playground.events

import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.emptyString
import java.io.BufferedReader
import java.io.InputStream
import java.io.StringReader
import java.util.Optional

internal class FakeHttpRequest(builder: Builder) : HttpRequest {

    private val method: String = builder.method
    override fun getMethod(): String = method

    private val uri: String = builder.uri
    override fun getUri(): String = uri

    private val path: String = builder.path
    override fun getPath(): String = path

    private val query: Optional<String> = Optional.ofNullable(builder.query)
    override fun getQuery(): Optional<String> = query

    private val queryParameters: Map<String, List<String>> = builder.queryParameters
    override fun getQueryParameters(): Map<String, List<String>> = queryParameters

    private val parts: Map<String, HttpRequest.HttpPart> = builder.parts
    override fun getParts(): Map<String, HttpRequest.HttpPart> = parts

    private val contentType: Optional<String> = Optional.ofNullable(builder.contentType)
    override fun getContentType(): Optional<String> = contentType

    private val contentLength: Long = builder.contentLength
    override fun getContentLength(): Long = contentLength

    private val characterEncoding: Optional<String> = Optional.ofNullable(builder.characterEncoding)
    override fun getCharacterEncoding(): Optional<String> = characterEncoding

    private val inputStream: InputStream = builder.inputStream
    override fun getInputStream(): InputStream = inputStream

    private val reader: BufferedReader = builder.reader
    override fun getReader(): BufferedReader = reader

    private val headers: Map<String, List<String>> = builder.headers
    override fun getHeaders(): Map<String, List<String>> = headers

    data class Builder(
        var method: String = "GET",
        var uri: String = "https://google.com/",
        var path: String = "",
        var query: String? = null,
        var queryParameters: Map<String, List<String>> = emptyMap(),
        var parts: Map<String, HttpRequest.HttpPart> = emptyMap(),
        var contentType: String? = null,
        var contentLength: Long = 0L,
        var characterEncoding: String? = null,
        var inputStream: InputStream = InputStream.nullInputStream(),
        var reader: BufferedReader = BufferedReader(StringReader(emptyString())),
        var headers: Map<String, List<String>> = emptyMap()
    ) {

        fun build() = FakeHttpRequest(this)
    }

    companion object {

        fun build(block: Builder.() -> Unit = { }) = Builder()
            .apply(block)
            .build()
    }
}
