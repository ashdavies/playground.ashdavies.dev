package io.ashdavies.playground

internal interface RatingsProvider {
    fun vote(items: List<RatingsItem>)
    fun ignore(item: RatingsItem)
}

internal fun RatingsProvider() = object : RatingsProvider {
    override fun vote(items: List<RatingsItem>) = Unit
    override fun ignore(item: RatingsItem) = Unit
}

internal fun interface RatingsService {
    suspend fun next(count: Int): List<RatingsItem>
}

internal data class RatingsItem(
    val name: String,
    val score: Long,
    val url: String,
    val id: String,
)
