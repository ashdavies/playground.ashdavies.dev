package io.ashdavies.playground.express

@JsNonModule
@JsModule("express")
external class Express {
    fun <T> get(route: String, callback: (req: Request, res: Response<T>) -> Unit)
}

external class Request

external class Response<T> {
    fun send(data: T): Response<T>
}
