package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ApplicationScope
import com.auth0.jwt.algorithms.Algorithm
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import com.google.firebase.FirebaseApp
import io.ashdavies.playground.cloud.HttpException
import io.ashdavies.playground.cloud.LocalApplicationScope
import io.ashdavies.playground.cloud.LocalHttpRequest
import io.ashdavies.playground.cloud.LocalHttpResponse

@Composable
public fun AppCheck(verify: Boolean = false, content: @Composable () -> Unit) {
    var isVerified by remember { mutableStateOf(!verify) }

    if (verify) {
        val scope: ApplicationScope = LocalApplicationScope.current
        val response: HttpResponse = LocalHttpResponse.current
        val appCheck: AppCheck = rememberAppCheck()
        val token = rememberAppCheckToken()

        LaunchedEffect(Unit) {
            try {
                appCheck.verifyToken(token) {}
                isVerified = true
            } catch (exception: HttpException) {
                response.setStatusCode(401, "Unauthorized")
                scope.exitApplication()
            }
        }
    }

    if (isVerified) {
        content()
    }
}

@Composable
public fun rememberAppCheck(algorithm: Algorithm = rememberAlgorithm()): AppCheck {
    return rememberAppCheck(rememberAppCheckClient(), algorithm)
}

@Composable
internal fun rememberAppCheck(client: AppCheckClient, algorithm: Algorithm = rememberAlgorithm()): AppCheck {
    return remember(client, algorithm) { AppCheck(client, algorithm) }
}

@Composable
private fun rememberAppCheckToken(request: HttpRequest = LocalHttpRequest.current): String = remember(request) {
    request.headers
        .getValue("X-FIREBASE-AppCheck")
        .first()
}
