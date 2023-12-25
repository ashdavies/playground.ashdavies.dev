package io.ashdavies.identity

import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.ashdavies.content.PlatformContext

internal actual class GoogleIdIdentityService actual constructor(
    private val context: PlatformContext,
) : IdentityService<GoogleIdIdentityRequest> {

    override suspend fun request(request: GoogleIdIdentityRequest): IdentityResponse {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(request.filterByAuthorizedAccounts)
            .setAutoSelectEnabled(request.autoSelectEnabled)
            .setServerClientId(request.serverClientId)
            .setNonce(request.nonce)
            .build()

        val getCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(context)
        val getCredentialResponse = credentialManager.getCredential(
            context = context,
            request = getCredentialRequest,
        )

        val googleIdCredential = when (val credential = getCredentialResponse.credential) {
            is GoogleIdTokenCredential -> credential

            is CustomCredential -> when (credential.type) {
                GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                    GoogleIdTokenCredential.createFrom(credential.data)
                }

                else -> throw UnsupportedOperationException()
            }

            else -> throw UnsupportedOperationException()
        }

        return IdentityResponse(
            uuid = googleIdCredential.idToken,
            pictureProfileUrl = googleIdCredential
                .profilePictureUri
                ?.toString(),
        )
    }
}
