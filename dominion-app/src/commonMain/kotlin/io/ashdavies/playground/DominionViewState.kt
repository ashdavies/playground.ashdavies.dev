package io.ashdavies.playground

import kotlinx.coroutines.flow.MutableStateFlow

internal sealed interface DominionViewState<out T : Any> {

    object Ready : DominionViewState<Nothing>
    object Loading : DominionViewState<Nothing>

    data class Success<T : Any>(val value: List<T>) : DominionViewState<T>
    data class Failure(val exception: Exception) : DominionViewState<Nothing>
}

internal suspend fun <T : Any> MutableStateFlow<DominionViewState<T>>.produceState(block: suspend () -> List<T>) {
    emit(DominionViewState.Loading)

    try {
        emit(DominionViewState.Success(block()))
    } catch (exception: Exception) {
        emit(DominionViewState.Failure(exception))
        exception.printStackTrace()
    }
}
