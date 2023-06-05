package io.ashdavies.playground

internal interface RatingsRegistry {
    suspend fun vote(items: List<RatingsItem>)
    suspend fun ignore(item: RatingsItem)
}

internal fun RatingsRegistry() = object : RatingsRegistry {
    override suspend fun vote(items: List<RatingsItem>) = Unit
    override suspend fun ignore(item: RatingsItem) = Unit
}
