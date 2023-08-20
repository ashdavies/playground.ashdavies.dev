package io.ashdavies.nsd

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

public actual class NsdManager

public actual class NsdServiceInfo {
    public actual fun getServiceName(): String {
        unsupportedOperation()
    }

    public actual fun getPort(): Int {
        unsupportedOperation()
    }
}

public actual fun NsdServiceInfo.getHostAddressOrNull(
    type: NsdHostAddress.Type,
): NsdHostAddress? = unsupportedOperation()

public actual fun NsdManager.discoverServices(
    serviceType: String,
    protocolType: Int,
): Flow<List<NsdServiceInfo>> = unsupportedOperation()

public actual fun NsdManager.resolveService(
    serviceInfo: NsdServiceInfo,
    coroutineDispatcher: CoroutineDispatcher,
): Flow<NsdServiceInfo> = unsupportedOperation()

private fun unsupportedOperation(): Nothing {
    error("UnsupportedOperation")
}
