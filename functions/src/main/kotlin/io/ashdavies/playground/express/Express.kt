package io.ashdavies.playground.express

@JsNonModule
@JsModule("express")
external class Express {
    fun <T> get(route: String, callback: (req: Request, res: Response<T>) -> Unit)
}

external interface Request {
    val query: dynamic
}

external interface Response<T> {
    fun status(code: StatusCode): Response<T>
    fun send(data: T): Response<T>
}

internal fun <T> Response<T>.error(
    code: StatusCode,
    message: String?,
): Response<T> = status(code).send(message.unsafeCast<T>())

internal typealias StatusCode = Int