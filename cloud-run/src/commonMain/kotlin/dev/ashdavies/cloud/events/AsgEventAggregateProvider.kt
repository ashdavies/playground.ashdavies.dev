package dev.ashdavies.cloud.events

import dev.ashdavies.asg.AsgService
import dev.ashdavies.cloud.toAttrs
import dev.ashdavies.cloud.toDomainEvent
import dev.ashdavies.http.common.models.DomainEvent
import dev.ashdavies.http.common.models.DomainWrapper
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject

@Inject
@ContributesIntoMap(AppScope::class)
@EventAggregateKey(EventAggregateSource.AndroidStudyGroup)
internal class AsgEventAggregateProvider(private val asgService: AsgService) : EventAggregateProvider {
    override suspend fun invoke(): List<DomainWrapper<DomainEvent>> = asgService { asgConference ->
        DomainWrapper(
            value = asgConference.toDomainEvent(),
            attrs = asgConference.toAttrs(),
        )
    }
}
