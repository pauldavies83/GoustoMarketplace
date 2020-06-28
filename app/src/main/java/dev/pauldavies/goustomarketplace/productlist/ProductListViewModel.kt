package dev.pauldavies.goustomarketplace.productlist

import androidx.hilt.lifecycle.ViewModelInject
import dev.pauldavies.goustomarketplace.base.BaseViewModel
import dev.pauldavies.goustomarketplace.base.Logger
import dev.pauldavies.goustomarketplace.base.requireValue
import dev.pauldavies.goustomarketplace.repository.Product
import dev.pauldavies.goustomarketplace.repository.ProductRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.text.NumberFormat
import java.util.*

internal class ProductListViewModel @ViewModelInject constructor(
    private val productRepository: ProductRepository,
    private val logger: Logger
) : BaseViewModel<ProductListViewModel.State>(State.Loading) {

    private val searchQuery = PublishSubject.create<String>()

    init {
        syncProducts()

        disposables += searchQuery.startWith("")
            .switchMap { query ->
                productRepository.products(query)
            }
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
            .subscribeBy(
                onError = { throwable ->
                    logger.debug(
                        tag = this.javaClass.simpleName,
                        message = "HttpErrorOccurred during sync",
                        throwable = throwable
                    )
                    if ((state.requireValue() as? State.Loaded)?.products?.isEmpty() == true) {
                        setState(State.NoResults)
                    }
                }
            )
    }

    fun onQueryChanged(newText: String?) {
        searchQuery.onNext(newText ?: "")
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
    imageUrl = imageUrl,
    ageRestricted = ageRestricted
)

private val currencyFormtter = NumberFormat.getCurrencyInstance().apply {
    maximumFractionDigits = 2
    currency = Currency.getInstance("GBP")
}
