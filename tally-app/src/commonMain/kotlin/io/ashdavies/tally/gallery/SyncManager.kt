package io.ashdavies.tally.gallery

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.update
import kotlinx.io.Source
import kotlinx.io.buffered
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.uuid.Uuid

internal interface SyncManager {
    val state: Flow<Map<Uuid, SyncState>>
    suspend fun sync(image: Image)
}

internal enum class SyncState {
    NOT_SYNCED,
    SYNCING,
    SYNCED,
}

@OptIn(ExperimentalAtomicApi::class)
internal fun SyncManager(client: HttpClient): SyncManager = object : SyncManager {

    private val _state = MutableStateFlow<Map<Uuid, SyncState>>(emptyMap())
    private val initialised = AtomicBoolean(false)

    override val state: Flow<Map<Uuid, SyncState>> = channelFlow {
        val isNotInitialised = initialised.compareAndSet(
            expectedValue = false,
            newValue = true,
        )

        if (isNotInitialised) {
            val initialValue = client.get("/").body<List<String>>()
            _state.value = initialValue.associate {
                Uuid.parse(it) to SyncState.SYNCED
            }
        }

        _state.collect(::send)
    }

    override suspend fun sync(image: Image) {
        val initialState = _state.value[image.uuid] ?: SyncState.NOT_SYNCED

        _state.update { it + (image.uuid to SyncState.SYNCING) }

        when (initialState) {
            SyncState.NOT_SYNCED -> client.post("${image.uuid}") {
                val body = SystemFileSystem.readString(image.path)
                header(HttpHeaders.ContentLength, body.length)
                setBody(body)
            }

            else -> client.put("${image.uuid}")
        }

        _state.update { it + (image.uuid to SyncState.SYNCED) }
    }
}

private fun FileSystem.readString(path: Path): String = source(path)
    .buffered()
    .use(Source::readString)
