package io.ashdavies.conferences

import platform.UIKit.UIDevice

actual object Platform {

    private val systemName: String
        get() = UIDevice
            .currentDevice
            .systemName

    private val systemVersion: String
        get() = UIDevice
            .currentDevice
            .systemVersion

    actual val platform: String =
        "$systemName $systemVersion"
}
