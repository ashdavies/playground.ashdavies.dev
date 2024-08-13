package io.ashdavies.cloud.operations

import io.ktor.server.application.ApplicationCall

internal interface UnaryOperation {
    suspend operator fun invoke(call: ApplicationCall)
}
