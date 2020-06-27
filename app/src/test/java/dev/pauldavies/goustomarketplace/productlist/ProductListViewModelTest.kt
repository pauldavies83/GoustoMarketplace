package dev.pauldavies.goustomarketplace.productlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.pauldavies.goustomarketplace.persistence.model.Product
import dev.pauldavies.goustomarketplace.repository.ProductRepository
import dev.pauldavies.goustomarketplace.util.RxSchedulerRule
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ProductListViewModelTest {

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val productId = "productId"
    private val productTitle = "product title"
    private val productPrice = 9.99
    private val productImageUrl = "https://image.url/1.jpg"
    private val product = Product(productId, productTitle, productPrice, productImageUrl)
    private val displayProductPrice = "£9.99"

    private val expectedItems = listOf(
        ProductListItem(productId, productTitle, displayProductPrice, productImageUrl)
    )

    private val productRepository = mock<ProductRepository> {
        whenever(it.syncProducts()).thenReturn(Completable.complete())
        whenever(it.products()).thenReturn(Observable.just(listOf(product)))
    }

    private val viewModel by lazy { ProductListViewModel(productRepository) }

    @Test
    fun `on start, sync products with api`() {
        viewModel

        verify(productRepository, times(1)).syncProducts()
    }

    @Test
    fun `products from repository mapped to state`() {
        viewModel.apply {
            assertEquals(expectedItems, (state.value as ProductListViewModel.State.Loaded).products)
        }
    }
}