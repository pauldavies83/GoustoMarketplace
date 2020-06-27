package dev.pauldavies.goustomarketplace.productlist

import androidx.hilt.lifecycle.ViewModelInject
import dev.pauldavies.goustomarketplace.base.BaseViewModel
import dev.pauldavies.goustomarketplace.repository.Product
import dev.pauldavies.goustomarketplace.repository.ProductRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import java.text.NumberFormat
import java.util.*

internal class ProductListViewModel @ViewModelInject constructor(
    private val productRepository: ProductRepository
) : BaseViewModel<ProductListViewModel.State>(State.Loading) {

    init {
        syncProducts()

        disposables += productRepository.products()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { products ->
                setState(
                    if (products.isEmpty()) {
                        State.NoResults
                    } else {
                        State.Loaded(
                            products.map { it.toProductListItem() }
                        )
                    }
                )
            }
    }

    private fun syncProducts() {
        disposables += productRepository.syncProducts()
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    sealed class State {
        object Loading : State()
        object NoResults : State()
        data class Loaded(val products: List<ProductListItem>) : State()
    }
}

private fun Product.toProductListItem() = ProductListItem(
    id = id,
    title = title,
    price = currencyFormtter.format(price),
    imageUrl = imageUrl
)

private val currencyFormtter = NumberFormat.getCurrencyInstance().apply {
    maximumFractionDigits = 2
    currency = Currency.getInstance("GBP")
}
