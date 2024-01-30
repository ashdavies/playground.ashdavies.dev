package io.ashdavies.http

public fun interface UnaryCallable<Request, Response> {
    public suspend operator fun invoke(request: Request): Response
}
