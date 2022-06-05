package io.ashdavies.playground.check

import io.ashdavies.playground.cloud.HttpException

internal class AppCheckClient {

    /**
     * public exchangeToken(customToken: string, appId: string): Promise<AppCheckToken> {
     *   if (!validator.isNonEmptyString(appId)) {
     *     throw new FirebaseAppCheckError(
     *       'invalid-argument',
     *       '`appId` must be a non-empty string.'
     *     );
     *   }
     *   if (!validator.isNonEmptyString(customToken)) {
     *     throw new FirebaseAppCheckError(
     *       'invalid-argument',
     *       '`customToken` must be a non-empty string.'
     *     );
     *   }
     *   return this.getUrl(appId)
     *     .then((url) => {
     *       const request: HttpRequestConfig = {
     *         method: 'POST',
     *         url,
     *         headers: FIREBASE_APP_CHECK_CONFIG_HEADERS,
     *         data: { customToken }
     *       };
     *       return this.httpClient.send(request);
     *     })
     *     .then((resp) => {
     *       return this.toAppCheckToken(resp);
     *     })
     *     .catch((err) => {
     *       throw this.toFirebaseError(err);
     *     });
     * }
     *
     * @see [app-check-api-client-internal.ts](https://github.com/firebase/firebase-admin-node/blob/4e816f44a3f3a67fcf912b6013c5beccb2210f8b/src/app-check/app-check-api-client-internal.ts#L62)
     */
    fun exchangeToken(customToken: String, appId: String): AppCheckToken = TODO()

    /**
     * private getUrl(appId: string): Promise<string> {
     *   return this.getProjectId()
     *     .then((projectId) => {
     *       const urlParams = {
     *         projectId,
     *         appId,
     *       };
     *
     *       const baseUrl = utils.formatString(FIREBASE_APP_CHECK_V1_API_URL_FORMAT, urlParams);
     *       return utils.formatString(baseUrl);
     *     });
     * }
     *
     * @see [app-check-api-client-internal.ts](https://github.com/firebase/firebase-admin-node/blob/4e816f44a3f3a67fcf912b6013c5beccb2210f8b/src/app-check/app-check-api-client-internal.ts#L91)
     */
    suspend fun getUrl(appId: String): String = TODO()
}

internal fun AppCheckError(message: String) = HttpException.BadRequest(message)
