package io.ashdavies.notion

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("workspace_id") val workspaceId: String,
    @SerialName("workspace_name") val workspaceName: String?,
    @SerialName("workspace_icon") val workspaceIcon: String?,
    @SerialName("bot_id") val botId: String,
    @SerialName("owner") val owner: Owner,
) {

    @Serializable
    data class Owner(@SerialName("workspace") val workspace: Boolean)
}
