package io.ashdavies.paging

/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.compose.runtime.Composable

@DslMarker
public annotation class LazyScopeMarker

/**
 * Receiver scope being used by the item content parameter of [LazyColumn].
 */
@LazyScopeMarker
public interface LazyItemScope

/**
 * Receiver scope.
 */
@LazyScopeMarker
public interface LazyListScope {
    /**
     * Adds a single item.
     *
     * @param itemId a stable and unique id representing the item. The value may not be less than
     * or equal to -2^62, as these values are reserved by the Glance API. Specifying the list
     * item ids will maintain the scroll position through app widget updates in Android S and
     * higher devices.
     * @param content the content of the item
     */
    public fun item(itemId: Long = UNSPECIFIED_ITEM_ID, content: @Composable LazyItemScope.() -> Unit)

    /**
     * Adds a [count] of items.
     *
     * @param count the count of items
     * @param itemId a factory of stable and unique ids representing the item. The value may not be
     * less than or equal to -2^62, as these values are reserved by the Glance API. Specifying
     * the list item ids will maintain the scroll position through app widget updates in Android
     * S and higher devices.
     * @param itemContent the content displayed by a single item
     */
    public fun items(
        count: Int,
        itemId: ((index: Int) -> Long) = { UNSPECIFIED_ITEM_ID },
        itemContent: @Composable LazyItemScope.(index: Int) -> Unit,
    )

    public companion object {
        internal const val UNSPECIFIED_ITEM_ID: Long = Long.MIN_VALUE
    }
}

/**
 * Adds a list of items.
 *
 * @param items the data list
 * @param itemId a factory of stable and unique ids representing the item. The value may not be
 * less than or equal to -2^62, as these values are reserved by the Glance API. Specifying
 * the list item ids will maintain the scroll position through app widget updates in Android
 * S and higher devices.
 * @param itemContent the content displayed by a single item
 */
internal inline fun <T> LazyListScope.items(
    items: List<T>,
    crossinline itemId: ((item: T) -> Long) = { LazyListScope.UNSPECIFIED_ITEM_ID },
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit,
) = items(items.size, { index: Int -> itemId(items[index]) }) {
    itemContent(items[it])
}

/**
 * Adds an array of items.
 *
 * @param items the data array
 * @param itemId a factory of stable and unique list item ids. Using the same itemId for multiple
 * items in the array is not allowed. When you specify the itemId, the scroll position will be
 * maintained based on the itemId, which means if you add/remove items before the current visible
 * item the item with the given itemId will be kept as the first visible one.
 * @param itemContent the content displayed by a single item
 */
internal inline fun <T> LazyListScope.items(
    items: Array<T>,
    noinline itemId: ((item: T) -> Long) = { LazyListScope.UNSPECIFIED_ITEM_ID },
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit,
) = items(items.size, { index: Int -> itemId(items[index]) }) {
    itemContent(items[it])
}
