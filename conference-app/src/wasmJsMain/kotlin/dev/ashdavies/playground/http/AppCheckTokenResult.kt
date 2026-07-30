@file:OptIn(ExperimentalWasmJsInterop::class)

package dev.ashdavies.playground.http

import kotlin.js.Promise

public external fun getAppCheckToken(forceRefresh: Boolean): Promise<AppCheckTokenResult>

public external interface AppCheckTokenResult : JsAny {
    public val token: String
}
