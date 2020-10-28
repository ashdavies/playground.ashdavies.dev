package io.ashdavies.playground.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.setContent

internal abstract class ComposeActivity(
    private val content: @Composable () -> Unit
) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUi = remember { SystemUi(window) }

            Providers(SystemUiAmbient provides systemUi) {
                content()
            }
        }
    }
}
