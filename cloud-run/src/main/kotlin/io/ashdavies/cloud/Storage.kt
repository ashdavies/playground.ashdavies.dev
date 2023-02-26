package io.ashdavies.cloud

import com.google.cloud.storage.StorageOptions

private val storageOptions: StorageOptions
    get() = StorageOptions.getDefaultInstance()

internal val storage by lazy(LazyThreadSafetyMode.NONE) {
    storageOptions.service
}
