package io.ashdavies.playground.express

internal class Express {
    fun <T> get(route: String, callback: (req: Request, res: Response<T>) -> Unit) = Unit
}

internal interface Request {
    val query: Any?
}

internal interface Response<T> {
    fun status(code: StatusCode): Response<T>
    fun send(data: T): Response<T>
}

@Suppress("UNCHECKED_CAST")
internal fun <T> Response<T>.error(
    code: StatusCode,
    message: String?,
): Response<T> = status(code).send(message as T)

internal typealias StatusCode = Int