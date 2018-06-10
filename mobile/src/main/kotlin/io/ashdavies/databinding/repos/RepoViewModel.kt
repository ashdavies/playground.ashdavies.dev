package io.ashdavies.databinding.repos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ashdavies.databinding.common.SingleLiveData
import io.ashdavies.databinding.extensions.create
import io.ashdavies.databinding.extensions.mutableLiveDataOf
import io.ashdavies.databinding.extensions.plusAssign
import io.ashdavies.databinding.extensions.singleLiveDataOf
import io.ashdavies.databinding.models.Repo
import io.ashdavies.databinding.services.GitHub
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit.MILLISECONDS

internal class RepoViewModel(service: GitHub) : ViewModel() {

  private val disposables = CompositeDisposable()
  private val query = PublishProcessor.create<String>()

  val items: MutableLiveData<List<Repo>> = mutableLiveDataOf()
  val loading: MutableLiveData<Boolean> = mutableLiveDataOf()
  val error: SingleLiveData<Throwable> = singleLiveDataOf()

  val hasItems: LiveData<Boolean> = map(loading) { !it && items.value?.isNotEmpty() ?: false }

  init {
    hasItems.observeForever { Log.e("RepoViewModel", "HasItems: $it") }

    disposables += query
        .doOnNext { loading.postValue(true) }
        .debounce(500, MILLISECONDS)
        .switchMapSingle(service::getRepos)
        .doOnNext { loading.postValue(false) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(items::setValue, error::setValue)
  }

  fun onQuery(value: String): Boolean {
    query.onNext(value)
    return true
  }

  class Factory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(kls: Class<T>): T {
      return RepoViewModel(retrofit.create()) as T
    }
  }
}
