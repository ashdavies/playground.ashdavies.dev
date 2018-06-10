package io.ashdavies.databinding.common

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

internal class SingleLiveData<T> : MutableLiveData<T>() {

  private val pending = AtomicBoolean(false)

  @MainThread
  override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
    if (hasActiveObservers()) {
      Log.w("SingleLiveData", "Multiple observers registered but only one will be notified of changes.")
    }

    super.observe(owner, Observer<T> { it ->
      if (pending.compareAndSet(true, false)) {
        observer.onChanged(it)
      }
    })
  }

  @MainThread
  override fun setValue(it: T?) {
    pending.set(true)
    super.setValue(it)
  }

  @MainThread
  operator fun invoke() {
    value = null
  }
}
