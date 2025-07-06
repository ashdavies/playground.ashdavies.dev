package io.ashdavies.identity

import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.ashdavies.content.PlatformContext

public actual fun GoogleIdIdentityService(context: PlatformContext): GoogleIdIdentityService {
    val credentialManager by lazy(LazyThreadSafetyMode.NONE) {
        CredentialManager.create(context)
    }

    return GoogleIdIdentityService { request ->
        val getCredentialResponse = try {
            credentialManager.getCredential(
                context = context,
                request = request,
                filterByAuthorizedAccounts = true,
            )
        } catch (ignored: GetCredentialException) {
            try {
                credentialManager.getCredential(
                    context = context,
                    request = request,
                    filterByAuthorizedAccounts = false,
                )
            } catch (ignored: GetCredentialException) {
                val signInWithGoogleOption = GetSignInWithGoogleOption
                    .Builder(request.serverClientId)
                    .setNonce(request.nonce)
                    .build()

                credentialManager.getCredential(
                    context = context,
                    option = signInWithGoogleOption,
                )
            }
        }

        when (val credential = getCredentialResponse.credential) {
            is GoogleIdTokenCredential -> IdentityResponse(
                uuid = credential.id,
                pictureProfileUrl = credential
                    .profilePictureUri
                    ?.toString(),
            )

            is CustomCredential -> {
                check(credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    "Unrecognised credential type ${credential.type}"
                }

                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                IdentityResponse(
                    uuid = googleIdTokenCredential.id,
                    pictureProfileUrl = googleIdTokenCredential
                        .profilePictureUri
                        ?.toString(),
                )
            }

            else -> throw UnsupportedOperationException("Unrecognised credential $credential")
        }
    }
}

private suspend fun CredentialManager.getCredential(
    context: PlatformContext,
    request: GoogleIdIdentityRequest,
    filterByAuthorizedAccounts: Boolean,
): GetCredentialResponse {
    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
        .setAutoSelectEnabled(request.autoSelectEnabled)
        .setServerClientId(request.serverClientId)
        .setNonce(request.nonce)
        .build()

    return getCredential(
        context = context,
        option = googleIdOption,
    )
}

private suspend fun CredentialManager.getCredential(
    context: PlatformContext,
    option: CredentialOption,
): GetCredentialResponse {
    val request = GetCredentialRequest.Builder()
        .addCredentialOption(option)
        .build()

    return getCredential(
        context = context,
        request = request,
    )
}
