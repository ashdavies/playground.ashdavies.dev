package io.ashdavies.tally.gallery

import kotlin.test.Test
import kotlin.test.assertEquals

class FileTest {
    @Test
    fun testGetAbsolutePath() {
        val path = "/images/test.jpg"
        val file = File(path)
        assertEquals(path, file.getAbsolutePath())
    }

    @Test
    fun testGetName() {
        val file = File("/images/test.jpg")
        assertEquals("test.jpg", file.getName())
    }

    @Test
    fun testGetNameWithNoSlash() {
        val file = File("test.jpg")
        assertEquals("test.jpg", file.getName())
    }

    @Test
    fun testLength() {
        val file = File("/images/test.jpg")
        assertEquals(0L, file.length())
    }
}
