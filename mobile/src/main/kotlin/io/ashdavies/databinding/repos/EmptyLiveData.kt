package io.ashdavies.databinding.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

internal class EmptyLiveData<in T>(items: LiveData<List<T>>, loading: LiveData<Boolean>) : MediatorLiveData<Boolean>() {

  init {
    addSource(items) { value = it?.isEmpty() ?: false && loading.value ?: false }
    addSource(loading) { value = it ?: false && items.value?.isEmpty() ?: false }
    value = true
  }
}
