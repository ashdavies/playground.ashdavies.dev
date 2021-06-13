package io.ashdavies.playground.configuration

import io.ashdavies.playground.store.Cache
import io.ashdavies.playground.store.Options

internal interface Environment {

    suspend fun getGithubToken(): String
}

internal fun Environment(): Environment {
    return ConfigurationEnvironment(ConfigurationCache())
}

private class ConfigurationEnvironment(
    private val cache: Cache<Configuration.Type, Configuration>,
) : Environment {

    override suspend fun getGithubToken(): String {
        val gitHubToken: String? = cache
            .read(Configuration.Type, Options())
            ?.gitHubToken

        return requireNotNull(gitHubToken) {
            "GitHub token not found in configuration cache"
        }
    }

}