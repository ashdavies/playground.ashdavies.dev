package io.ashdavies.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.firebase.FirebaseApp
import io.ashdavies.check.getProjectId
import io.ashdavies.playground.cloud.LocalFirebaseApp

@Composable
public fun rememberProjectId(
    app: FirebaseApp = LocalFirebaseApp.current
): String = remember(app) {
    getProjectId(app)
}
