package io.ashdavies.notion

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

internal class CallbackActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _PIPE.trySend(intent.data)
    }

    companion object {
        @Suppress("ktlint:standard:property-naming")
        private val _PIPE = Channel<Uri?>(Channel.CONFLATED)
        val PIPE: Flow<Uri?> = _PIPE.receiveAsFlow()
    }
}
