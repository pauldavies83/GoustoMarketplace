package dev.pauldavies.goustomarketplace.productlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dev.pauldavies.goustomarketplace.repository.Product
import dev.pauldavies.goustomarketplace.repository.ProductRepository
import dev.pauldavies.goustomarketplace.util.RxSchedulerRule
import io.reactivex.rxjava3.core.Single
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
        whenever(it.products()).thenReturn(Single.just(listOf(product)))
    }

    @Test
    fun `products from repository mapped to state`() {
        ProductListViewModel(productRepository).apply {
            assertEquals(expectedItems, (state.value as ProductListViewModel.State.Loaded).products)
        }
    }
}