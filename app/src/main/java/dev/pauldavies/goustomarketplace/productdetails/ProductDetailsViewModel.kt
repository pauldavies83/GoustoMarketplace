package dev.pauldavies.goustomarketplace.productdetails

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import dev.pauldavies.goustomarketplace.base.BaseViewModel
import dev.pauldavies.goustomarketplace.repository.ProductRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

const val PRODUCT_ID_KEY = "PRODUCT_ID_KEY"

internal class ProductDetailsViewModel @ViewModelInject constructor(
    productRepository: ProductRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : BaseViewModel<ProductDetailsViewModel.State, Unit>(State.Loading) {

    private val productId = savedStateHandle.get<String>(PRODUCT_ID_KEY)!!

    init {
        disposables += productRepository.product(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    setState(State.Loaded(it.title))
                }
            )
    }

    sealed class State {
        object Loading: State()
        data class Loaded(val title: String): State()
    }

}