package io.ashdavies.playground.cloud

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import io.ashdavies.check.AppCheck
import io.ashdavies.check.rememberAppCheck

@Composable
public fun AppCheck(verify: Boolean = false, content: @Composable () -> Unit) {
    if (verify) AppCheck(content) else content()
}

@Composable
private fun AppCheck(content: @Composable () -> Unit) {
    var isValid by remember { mutableStateOf(false) }

    val scope: ApplicationScope = LocalApplicationScope.current
    val response: HttpResponse = LocalHttpResponse.current
    val appCheck: AppCheck = rememberAppCheck()
    val token = rememberAppCheckToken()

    LaunchedEffect(Unit) {
        try {
            appCheck.verifyToken(token) {}
            isValid = true
        } catch (exception: HttpException) {
            response.setStatusCode(401, "Unauthorized")
            scope.exitApplication()
        }
    }

    if (isValid) {
        content()
    }
}

@Composable
private fun rememberAppCheckToken(request: HttpRequest = LocalHttpRequest.current): String = remember(request) {
    request.headers
        .getValue("X-FIREBASE-AppCheck")
        .first()
}
