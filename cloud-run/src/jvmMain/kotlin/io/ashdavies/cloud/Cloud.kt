package io.ashdavies.cloud

import com.google.cloud.Service
import com.google.cloud.ServiceOptions
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions

private fun <S : Service<O>, O : ServiceOptions<S, O>> service(options: () -> O) =
    lazy(LazyThreadSafetyMode.NONE) { options().service }

internal val firestore: Firestore
    by service(FirestoreOptions::getDefaultInstance)

internal val storage: Storage
    by service(StorageOptions::getDefaultInstance)
