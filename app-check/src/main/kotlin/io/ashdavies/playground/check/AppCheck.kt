package io.ashdavies.playground.check

internal class AppCheck(
    private val client: AppCheckClient,
    private val generator: AppCheckTokenGenerator,
) : AppCheckInterface {

    override suspend fun createToken(appId: String, options: AppCheckTokenOptions?): AppCheckToken {
        return client.exchangeToken(generator.createCustomToken(appId, options), appId)
    }

    override suspend fun verifyToken(appCheckToken: String): VerifyAppCheckTokenResponse {
        TODO("Not yet implemented")
    }
}
