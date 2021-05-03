package io.ashdavies.playground.service

import io.ashdavies.playground.express.Request
import io.ashdavies.playground.express.Response
import io.ashdavies.playground.github.GitHubService

internal val ConferencesService: (Request, Response<List<Any?>>) -> Unit =
    coroutineService { req, res ->
        val gitHubService = GitHubService(req.query.token as String)
        val conferences: List<Any?> = gitHubService.conferences()
        res.send(conferences)
    }