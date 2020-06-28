package dev.pauldavies.goustomarketplace.view.details

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import dev.pauldavies.goustomarketplace.base.BaseViewModel
import dev.pauldavies.goustomarketplace.repository.ProductRepository
import dev.pauldavies.goustomarketplace.view.list.currencyFormtter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

const val PRODUCT_ID_KEY = "PRODUCT_ID_KEY"

internal class ProductDetailsViewModel @ViewModelInject constructor(
    private val productRepository: ProductRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : BaseViewModel<ProductDetailsViewModel.State, Unit>(State.Loading) {

    init {
        val productId = savedStateHandle.get<String>(PRODUCT_ID_KEY)
        if (productId == null) {
            setState(State.Error)
        } else {
            loadProduct(productId)
        }
    }

    private fun loadProduct(productId: String) {
        disposables += productRepository.product(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    setState(
                        State.Loaded(
                            title = it.title,
                            price = currencyFormtter.format(it.price),
                            imageUrl = it.imageUrl,
                            ageRestricted = it.ageRestricted,
                            categories = it.categories
                        )
                    )
                },
                onError = { setState(State.Error) }
            )
    }

    sealed class State {
        object Loading : State()
        object Error : State()
        data class Loaded(
            val title: String,
            val description: String? = null,
            val price: String,
            val imageUrl: String?,
            val ageRestricted: Boolean,
            val categories: List<String>
        ) : State()
    }

}