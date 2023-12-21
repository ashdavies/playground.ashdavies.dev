package io.ashdavies.identity

import androidx.datastore.core.Serializer
import com.squareup.wire.ProtoAdapter
import java.io.InputStream
import java.io.OutputStream

internal class ProtoSerializer<T>(
    private val adapter: ProtoAdapter<T>,
    private val default: () -> T
) : Serializer<T> {

    override val defaultValue: T
        get() = default()

    override suspend fun readFrom(input: InputStream): T {
        return adapter.decode(input)
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        adapter.encode(output, t)
    }
}
