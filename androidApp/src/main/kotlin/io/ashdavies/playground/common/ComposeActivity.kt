package io.ashdavies.playground.common

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

internal abstract class ComposeActivity(
    private val content: @Composable () -> Unit
) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUi = remember { SystemUi(window) }
            CompositionLocalProvider(LocalSystemUi provides systemUi) {
                content()
            }
        }
    }
}
