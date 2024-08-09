package io.ashdavies.http

public fun interface UnaryCallable<Request, Response> {
    public suspend operator fun invoke(request: Request): Response
}

public suspend operator fun <Response> UnaryCallable<Unit, Response>.invoke(): Response {
    return invoke(Unit)
}
