package io.ashdavies.notion

internal fun AuthResponseQueries.insert(accessToken: String) =
    insert(
        accessToken = accessToken,
        workspaceId = UuidString(),
        workspaceName = null,
        workspaceIcon = null,
        botId = UuidString(),
        owner = Owner(),
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

private fun UuidString(): String =
    UuidValue
        .randomUuid()
        .toString()

private fun Owner() =
    AuthResponse
        .Owner(true)
