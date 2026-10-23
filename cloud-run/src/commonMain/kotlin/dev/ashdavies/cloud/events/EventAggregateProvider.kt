package dev.ashdavies.cloud.events

import dev.ashdavies.http.common.models.DomainEvent
import dev.ashdavies.http.common.models.DomainWrapper

internal fun interface EventAggregateProvider {
    suspend operator fun invoke(): List<DomainWrapper<DomainEvent>>
}
