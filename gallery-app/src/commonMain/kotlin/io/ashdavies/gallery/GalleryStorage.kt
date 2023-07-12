package io.ashdavies.gallery

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class GalleryStorage {

    private val _itemList = MutableStateFlow(emptyList<Uri>())
    private val itemList = _itemList.asStateFlow()

    fun items(): Flow<List<Uri>> = itemList

    fun store(item: Uri) {
        _itemList.update { it + item }
    }
}
