package io.ashdavies.check

import com.google.cloud.functions.HttpMessage

private const val X_FIREBASE_APP_CHECK = "X-Firebase-AppCheck"

public val HttpMessage.appCheckToken: String?
    get() = headers[X_FIREBASE_APP_CHECK]?.firstOrNull()

internal sealed interface GrantType {
    val value: Any?
}
