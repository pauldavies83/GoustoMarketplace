package dev.pauldavies.goustomarketplace.productlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pauldavies.goustomarketplace.repository.ProductRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign

internal class ProductListViewModel @ViewModelInject constructor(
    productRepository: ProductRepository
): ViewModel() {

    private val disposables = CompositeDisposable()

    private val _state = MutableLiveData<State>(State.Loading)
    val state: LiveData<State> = _state

    init {
        disposables += productRepository.products()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _state.value = State.Loaded(it.first().title)
        }
    }

    sealed class State {
        object Loading: State()
        data class Loaded(val title: String): State()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}