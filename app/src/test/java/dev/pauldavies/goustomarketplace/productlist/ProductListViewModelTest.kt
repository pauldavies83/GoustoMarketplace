package dev.pauldavies.goustomarketplace.productlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.pauldavies.goustomarketplace.repository.ProductRepository
import dev.pauldavies.goustomarketplace.util.RxSchedulerRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ProductListViewModelTest {

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val productRepository = ProductRepository()

    @Test
    fun `first test`() {
        ProductListViewModel(productRepository).apply {
            assertEquals("Hello World", (state.value as ProductListViewModel.State.Loaded).title)
        }
    }
}