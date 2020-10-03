package io.ashdavies.playground.conferences

import com.dropbox.android.external.store4.SourceOfTruth

internal typealias ListingSourceOfTruth<K, T> = SourceOfTruth<K, List<T>, List<T>>
