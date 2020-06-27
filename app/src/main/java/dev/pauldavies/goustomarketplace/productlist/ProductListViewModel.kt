package dev.pauldavies.goustomarketplace.productlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pauldavies.goustomarketplace.persistence.model.Product
import dev.pauldavies.goustomarketplace.repository.ProductRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.text.NumberFormat
import java.util.*

internal class ProductListViewModel @ViewModelInject constructor(
    productRepository: ProductRepository
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _state = MutableLiveData<State>(State.Loading)
    val state: LiveData<State> = _state

    init {
        disposables += productRepository.syncProducts()
            .subscribeOn(Schedulers.io())
            .subscribe()

        disposables += productRepository.products()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { products ->
                _state.value = State.Loaded(
                    products.map { it.toProductListItem() }
                )
            }
    }

    sealed class State {
        object Loading : State()
        data class Loaded(val products: List<ProductListItem>) : State()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}

private fun Product.toProductListItem() = ProductListItem(
    id = id,
    title = title,
    price = currencyFormtter.format(price), // format currency
    imageUrl = imageUrl
)

private val currencyFormtter = NumberFormat.getCurrencyInstance().apply {
    maximumFractionDigits = 2
    currency = Currency.getInstance("GBP")
}
