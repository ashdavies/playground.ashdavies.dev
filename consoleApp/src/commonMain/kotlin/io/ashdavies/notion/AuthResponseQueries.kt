package io.ashdavies.notion

internal fun AuthResponseQueries.insert(accessToken: String) =
    insert(
        owner = AuthResponse.Owner(true),
        accessToken = accessToken,
        workspaceId = randomUuid(),
        botId = randomUuid(),
        workspaceName = null,
        workspaceIcon = null,
    )

internal fun AuthResponseQueries.insert(response: AuthResponse) =
    insert(
        accessToken = response.accessToken,
        workspaceId = response.workspaceId,
        workspaceName = response.workspaceName,
        workspaceIcon = response.workspaceIcon,
        botId = response.botId,
        owner = response.owner,
    )
