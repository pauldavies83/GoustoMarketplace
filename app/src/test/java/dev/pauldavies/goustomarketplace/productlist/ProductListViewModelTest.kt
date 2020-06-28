package dev.pauldavies.goustomarketplace.productlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import dev.pauldavies.goustomarketplace.base.Logger
import dev.pauldavies.goustomarketplace.base.requireValue
import dev.pauldavies.goustomarketplace.repository.Product
import dev.pauldavies.goustomarketplace.repository.ProductRepository
import dev.pauldavies.goustomarketplace.util.RxSchedulerRule
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class ProductListViewModelTest {

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val productId = "productId"
    private val productTitle = "product title"
    private val productPrice = 9.99
    private val productImageUrl = "https://image.url/1.jpg"
    private val productAgeRestricted = true
    private val product =
        Product(productId, productTitle, productPrice, productImageUrl, productAgeRestricted, emptyList())
    private val displayProductPrice = "£9.99"

    private val expectedItems = listOf(
        ProductListItem(
            productId,
            productTitle,
            displayProductPrice,
            productImageUrl,
            productAgeRestricted
        )
    )

    private val productRepository = mock<ProductRepository> {
        whenever(it.syncProducts()).thenReturn(Completable.complete())
        whenever(it.products(any())).thenReturn(Observable.just(listOf(product)))
    }
    private val logger = mock<Logger>()
    private val viewModel by lazy { ProductListViewModel(productRepository, logger) }

    @Test
    fun `on start, sync products with api`() {
        viewModel

        verify(productRepository, times(1)).syncProducts()
    }

    @Test
    fun `when error on sync products with api, error is logged`() {
        val throwable = Throwable()
        productRepository.stub { whenever(it.syncProducts()).thenReturn(Completable.error(throwable)) }

        viewModel

        verify(logger, times(1)).debug(anyString(), anyString(), eq(throwable))
    }

    @Test
    fun `when error on first sync products with api, state is no results`() {
        val throwable = Throwable()
        productRepository.stub {
            whenever(it.syncProducts()).thenReturn(Completable.error(throwable))
            whenever(it.products(any())).thenReturn(Observable.just(emptyList()))
        }

        viewModel.apply {
            assertTrue(state.requireValue() is ProductListViewModel.State.NoResults)
        }
    }

    @Test
    fun `on start, state is loading`() {
        productRepository.stub { whenever(it.products(any())).thenReturn(Observable.empty()) }
        viewModel.apply {
            assertTrue(state.requireValue() is ProductListViewModel.State.Loading)
        }
    }

    @Test
    fun `when no products from repository, state is no results`() {
        productRepository.stub { whenever(it.products(any())).thenReturn(Observable.just(emptyList())) }
        viewModel.apply {
            assertTrue(state.requireValue() is ProductListViewModel.State.NoResults)
        }
    }

    @Test
    fun `products from repository mapped to loaded state`() {
        viewModel.apply {
            assertEquals(
                expectedItems,
                (state.requireValue() as ProductListViewModel.State.Loaded).products
            )
        }
    }

    @Test
    fun `when search query changed, passed to repository`() {
        viewModel.apply {
            onQueryChanged("query")

            verify(productRepository).products("query")
        }
    }
}