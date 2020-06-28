package dev.pauldavies.goustomarketplace.base

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

internal open class BaseViewModel<S, E>(initialState: S) : ViewModel() {

    protected val disposables = CompositeDisposable()

    private val _state: MutableLiveData<S> = MutableLiveData<S>(initialState)
    val state: LiveData<S> = _state

    private val _events = MutableLiveData<SingleEvent<E>>()
    val events: LiveData<SingleEvent<E>> = _events

    @MainThread
    protected fun setState(newState: S) {
        if (_state.value != newState) _state.value = newState
    }

    @MainThread
    protected fun sendEvent(event: E) {
        _events.value = SingleEvent(event)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}

fun <T : Any> LiveData<T>.requireValue(): T = value!!
fun emptyString() = ""


