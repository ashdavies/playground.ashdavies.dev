package io.ashdavies.playground

internal interface RatingsProvider<T : Any> {
    fun vote(items: List<RatingsItem>)
    fun ignore(item: RatingsItem)
}

internal fun RatingsProvider() = object : RatingsProvider<String> {
    override fun vote(items: List<RatingsItem>) = Unit
    override fun ignore(item: RatingsItem) = Unit
}

internal fun interface RatingsService<T : Any> {
    suspend fun next(count: Int): List<RatingsItem>
}

internal data class RatingsItem(
    val name: String,
    val score: Long,
    val url: String,
    val id: String,
)
