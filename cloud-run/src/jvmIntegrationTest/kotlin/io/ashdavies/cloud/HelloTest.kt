package io.ashdavies.cloud

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.fail
import kotlin.test.assertEquals

internal class HelloTest {

    @Test
    fun `should create test application`() = testApplication {
        val response = client.get("/hello")

        assertEquals(
            expected = HttpStatusCode.OK,
            actual = response.status,
        )

        assertEquals(
            actual = response.bodyAsText(),
            expected = "Hello, World!",
        )
    }

    @Test
    fun `should fail`() = {
        fail()
    }
}
