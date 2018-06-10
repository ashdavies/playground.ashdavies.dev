package io.ashdavies.databinding.extensions

import androidx.lifecycle.MutableLiveData

internal fun <T> mutableLiveDataOf(): MutableLiveData<T> = MutableLiveData()
