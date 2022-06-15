package io.ashdavies.playground.events

import com.google.api.client.http.HttpStatusCodes
import com.google.cloud.functions.HttpResponse
import java.io.BufferedWriter
import java.io.OutputStream
import java.util.Optional

internal class FakeHttpResponse : HttpResponse{
    override fun getWriter(): BufferedWriter = BufferedWriter.nullWriter().buffered()
    override fun getOutputStream(): OutputStream = OutputStream.nullOutputStream()
    override fun getHeaders(): Map<String, MutableList<String>> = emptyMap()
    override fun getContentType(): Optional<String> = Optional.empty()
    override fun appendHeader(header: String, value: String) = Unit
    override fun setStatusCode(code: Int, message: String) = Unit
    override fun setContentType(contentType: String) = Unit
    override fun setStatusCode(code: Int) = Unit
}
