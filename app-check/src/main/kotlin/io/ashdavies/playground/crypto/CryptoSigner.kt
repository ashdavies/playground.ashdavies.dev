package io.ashdavies.playground.crypto

import io.ashdavies.playground.credential.Credential

internal sealed interface CryptoSigner {

    val accountId: String
    val algorithm: String

    /**
     * https://github.com/firebase/firebase-admin-node/blob/4e816f44a3f3a67fcf912b6013c5beccb2210f8b/src/utils/crypto-signer.ts#L128
     *
     * public sign(buffer: Buffer): Promise<Buffer> {
     *   return this.getAccountId().then((serviceAccount) => {
     *     const request: HttpRequestConfig = {
     *       method: 'POST',
     *       url: `https://iamcredentials.googleapis.com/v1/projects/-/serviceAccounts/${serviceAccount}:signBlob`,
     *       data: { payload: buffer.toString('base64') },
     *     };
     *     return this.httpClient.send(request);
     *   }).then((response: any) => {
     *     // Response from IAM is base64 encoded. Decode it into a buffer and return.
     *     return Buffer.from(response.data.signedBlob, 'base64');
     *   }).catch((err) => {
     *     if (err instanceof HttpError) {
     *       throw new CryptoSignerError({
     *         code: CryptoSignerErrorCode.SERVER_ERROR,
     *         message: err.message,
     *         cause: err
     *       });
     *     }
     *     throw err
     *   });
     * }
     */
    fun sign(token: String): String = TODO()

    data class ServiceAccountSigner(
        private val credential: Credential.ServiceAccountCredential,
        override val accountId: String = credential.clientEmail,
        override val algorithm: String = "ALGORITHM_RS256",
    ) : CryptoSigner

    data class IamSigner(
        private val credential: Credential.ServiceAccountCredential,
        override val accountId: String = credential.serviceAccountId,
        override val algorithm: String = "ALGORITHM_RS256",
    ) : CryptoSigner
}
