package dev.ashdavies.cloud.events

import dev.ashdavies.cloud.google.attrs
import dev.ashdavies.playground.event.EventFixtures
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.uuid.Uuid

internal class FirebaseEventWriterTest {

    private val data = EventFixtures.upcoming { it }

    @Test
    fun `should write events with attributes`() = runTest {
        TestFirestoreWriter().let { writer ->
            FirebaseEventWriter({ writer }, coroutineContext).write(
                source = EventAggregateSource.AndroidStudyGroup,
                value = data,
            )

            data.zip(writer.documents.values) { (attrs, event), document ->
                assertEquals(EventAggregateSource.AndroidStudyGroup.name, document["_source"])
                assertEquals(event.uuid, Uuid.parse(document["uuid"] as String))

                attrs.forEach { (key, value) ->
                    assertEquals(document.attrs[key], value)
                }
            }

            assertTrue(writer.documents.isNotEmpty())
            assertEquals(data.size, writer.writes)
            assertTrue(writer.isClosed)
        }
    }

    @Test
    fun `should update events with added attributes`() = runTest {
        TestFirestoreWriter().let { writer ->
            FirebaseEventWriter({ writer }, coroutineContext).write(
                source = EventAggregateSource.AndroidStudyGroup,
                value = data,
            )

            val unmodifiedData = data.take(data.size - 3)
            val modifiedData = data.takeLast(3).map {
                it.copy(attrs = it.attrs + ("updated" to "${true}"))
            }

            TestFirestoreWriter(writer.documents).let { writer ->
                FirebaseEventWriter({ writer }, coroutineContext).write(
                    source = EventAggregateSource.AndroidStudyGroup,
                    value = unmodifiedData + modifiedData,
                )

                assertEquals(3, writer.writes)
                assertTrue(writer.isClosed)
            }
        }
    }

    @Test
    fun `should delete events removed from source`() = runTest {
        TestFirestoreWriter().let { writer ->
            FirebaseEventWriter({ writer }, coroutineContext).write(
                source = EventAggregateSource.AndroidStudyGroup,
                value = data,
            )

            val modifiedData = data.take(3)

            TestFirestoreWriter(writer.documents).let { writer ->
                FirebaseEventWriter({ writer }, coroutineContext).write(
                    source = EventAggregateSource.AndroidStudyGroup,
                    value = modifiedData,
                )

                modifiedData.zip(writer.documents.values) { (_, event), document ->
                    assertEquals(event.uuid, Uuid.parse(document["uuid"] as String))
                }

                assertEquals(data.size - modifiedData.size, writer.writes)
                assertEquals(writer.documents.size, modifiedData.size)
                assertTrue(writer.isClosed)
            }
        }
    }
}
