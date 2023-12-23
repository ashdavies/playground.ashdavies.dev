package io.ashdavies.identity

import org.junit.Test
import kotlin.test.assertEquals

internal class ProtoSerializerTest {

    @Test
    fun `should serialize credential`() {
        val credential = Credential(profile_picture_url = "https://picsum.photos/200")

        val encoded = Credential.ADAPTER.encode(credential)
        val decoded = Credential.ADAPTER.decode(encoded)

        assertEquals(credential, decoded)
    }

    @Test
    fun `should deserialize empty byte array`() {
        println(Credential.ADAPTER.decode(byteArrayOf()))
    }
}
