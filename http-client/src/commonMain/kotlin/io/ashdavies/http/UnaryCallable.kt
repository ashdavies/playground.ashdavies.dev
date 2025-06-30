package io.ashdavies.http

public fun interface UnaryCallable<Request, Response> {
    public suspend operator fun invoke(request: Request): Response
}

public suspend operator fun <Response> UnaryCallable<Unit, Response>.invoke(): Response {
    return invoke(Unit)
}

public suspend fun <Request, Response, Result> UnaryCallable<Request, List<Response>>.asSequence(
    request: Request,
    block: (Sequence<Response>) -> Sequence<Result>,
): List<Result> = invoke(request)
    .asSequence()
    .run(block)
    .toList()
