package dev.pauldavies.goustomarketplace.productlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pauldavies.goustomarketplace.repository.ProductRepository

internal class ProductListViewModel @ViewModelInject constructor(
    productRepository: ProductRepository
): ViewModel() {

    val state = MutableLiveData<State>(
        State.Loading
    )

    init {
        state.value = State.Loaded(productRepository.products().first().title)
    }

    sealed class State {
        object Loading: State()
        data class Loaded(val title: String): State()
    }
}