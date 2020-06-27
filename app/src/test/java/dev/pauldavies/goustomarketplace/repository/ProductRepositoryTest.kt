package dev.pauldavies.goustomarketplace.repository

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dev.pauldavies.goustomarketplace.api.ApiProduct
import dev.pauldavies.goustomarketplace.api.ApiProductResponse
import dev.pauldavies.goustomarketplace.api.GoustoApi
import io.reactivex.rxjava3.core.Single
import org.junit.Test

class ProductRepositoryTest {

    private val productId = "productId"
    private val productTitle = "product title"
    private val productPrice = 9.99
    private val expectedProducts = listOf(Product(productId, productTitle, productPrice))

    private val response = ApiProductResponse(listOf(ApiProduct(id = productId, title = productTitle, list_price =  productPrice)))
    private val goustoApi = mock<GoustoApi> {
        whenever(it.getProducts()).thenReturn(Single.just(response))
    }

    private val repository by lazy { ProductRepository(goustoApi) }

    @Test
    fun `api response mapped to domain model`() {
        repository.products().test()
            .assertResult(expectedProducts)
    }
}