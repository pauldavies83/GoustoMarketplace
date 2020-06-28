package dev.pauldavies.goustomarketplace.view.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stub
import com.nhaarman.mockitokotlin2.whenever
import dev.pauldavies.goustomarketplace.base.requireValue
import dev.pauldavies.goustomarketplace.repository.ProductRepository
import dev.pauldavies.goustomarketplace.util.ProductCreator
import dev.pauldavies.goustomarketplace.util.RxSchedulerRule
import dev.pauldavies.goustomarketplace.view.list.currencyFormtter
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ProductDetailsViewModelTest {

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val product = ProductCreator.product()
    private val productRepository = mock<ProductRepository>()
    private val savedStateHandle = SavedStateHandle().apply {
        set("PRODUCT_ID_KEY", "productId")
    }

    private val viewModel by lazy { ProductDetailsViewModel(productRepository, savedStateHandle) }

    @Test
    fun `when no productId provided show error`() {
        ProductDetailsViewModel(productRepository, SavedStateHandle()).apply {
            assertTrue(state.requireValue() is ProductDetailsViewModel.State.Error)
        }
    }

    @Test
    fun `when repository returns error show error`() {
        productRepository.stub {
            whenever(it.product(any())).thenReturn(Single.error(Throwable()))
        }

        viewModel.apply {
            assertTrue(state.requireValue() is ProductDetailsViewModel.State.Error)
        }
    }

    @Test
    fun `when repository returns product set state`() {
        productRepository.stub {
            whenever(it.product(any())).thenReturn(Single.just(product))
        }

        viewModel.apply {
            val expectedState = ProductDetailsViewModel.State.Loaded(
                title = product.title,
                description = product.description,
                price = currencyFormtter.format(product.price),
                imageUrl = product.imageUrl,
                ageRestricted = product.ageRestricted,
                categories = product.categories
            )
            assertEquals(expectedState, requireLoadedState())
        }
    }

    private fun ProductDetailsViewModel.requireLoadedState() =
        (state.requireValue() as ProductDetailsViewModel.State.Loaded)
}
