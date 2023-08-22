package io.ashdavies.gallery

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.atomic.AtomicBoolean

internal interface SyncManager {
    fun state(): Flow<Map<String, SyncState>>
    suspend fun sync(path: String)
}

internal enum class SyncState {
    NOT_SYNCED,
    SYNCING,
    SYNCED,
}

internal fun SyncManager(
    client: HttpClient,
    reader: File.() -> ByteReadChannel = File::readChannel,
): SyncManager = object : SyncManager {

    private val state = MutableStateFlow<Map<String, SyncState>>(emptyMap())
    private val initialised = AtomicBoolean(false)

    override fun state(): Flow<Map<String, SyncState>> = channelFlow {
        if (initialised.compareAndSet(false, true)) {
            val initialValue = client.get("/").body<List<String>>()
            state.value = initialValue.associateWith { SyncState.SYNCED }
        }

        state.collect(::send)
    }

    override suspend fun sync(path: String) = with(File(path)) {
        val initialState = state.value[getName()] ?: SyncState.NOT_SYNCED
        println(state.value)

        state.update { it + (getName() to SyncState.SYNCING) }

        when (initialState) {
            SyncState.NOT_SYNCED -> client.post(getName()) {
                header(HttpHeaders.ContentLength, length())
                setBody(reader())
            }

            else -> client.put(getName())
        }

        state.update { it + (getName() to SyncState.SYNCED) }
    }
}

internal expect fun File.readChannel(): ByteReadChannel
