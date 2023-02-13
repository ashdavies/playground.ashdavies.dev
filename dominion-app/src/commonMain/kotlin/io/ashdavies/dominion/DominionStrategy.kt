package io.ashdavies.dominion

/**
 * Dominion Strategy uses legacy wiki API without SSL with an API surface that is difficult to navigate.
 * Move necessary requests to Firebase Firestore and Cloud Functions.
 */
@Deprecated("dominionstrategy.com is deprecated")
internal const val DOMINION_STRATEGY_URL = "http://wiki.dominionstrategy.com/api.php"
