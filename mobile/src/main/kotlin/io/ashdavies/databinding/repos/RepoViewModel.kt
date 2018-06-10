package io.ashdavies.databinding.repos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ashdavies.databinding.extensions.mutableLiveDataOf
import io.ashdavies.databinding.extensions.plusAssign
import io.ashdavies.databinding.models.Repo
import io.ashdavies.databinding.services.GitHub
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

internal class RepoViewModel(private val service: GitHub) : ViewModel() {

  private val disposables = CompositeDisposable()

  val items = mutableLiveDataOf<List<Repo>>()
  val error = mutableLiveDataOf<Throwable>()

  fun getRepos(user: String) {
    disposables += service.getRepos(user)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { items.value = it },
            { error.value = it }
        )
  }

  class Factory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(kls: Class<T>): T {
      return RepoViewModel(retrofit.create()) as T
    }
  }

  companion object {

    private const val GITHUB_API = "https://api.github.com"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(GITHUB_API)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private inline fun <reified T> Retrofit.create(): T = create(T::class.java)
  }
}
