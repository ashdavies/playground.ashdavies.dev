package io.ashdavies.playground.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.ashdavies.architecture.Event

internal class MainViewModel : ViewModel() {

  private val _errors: MutableLiveData<Event<Throwable>> = MutableLiveData()
  val errors: LiveData<Event<Throwable>> = _errors

  fun onError(throwable: Throwable) {
    _errors.value = Event(throwable)
  }
}
