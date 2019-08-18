package io.ashdavies.databinding.extensions

import androidx.lifecycle.MutableLiveData
import io.ashdavies.databinding.common.SingleLiveData

internal fun <T> mutableLiveDataOf(): MutableLiveData<T> = MutableLiveData()
internal fun <T> singleLiveDataOf(): SingleLiveData<T> = SingleLiveData()
