package io.ashdavies.playground.express

@JsNonModule
@JsModule("express")
internal external class Express {
    fun <T> get(route: String, callback: (req: Request, res: Response<T>) -> Unit)
}

internal external interface Request {
    val query: dynamic
}

internal external interface Response<T> {
    fun status(code: StatusCode): Response<T>
    fun send(data: T): Response<T>
}

internal typealias StatusCode = Int
