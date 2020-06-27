package dev.pauldavies.goustomarketplace.repository

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dev.pauldavies.goustomarketplace.api.*
import io.reactivex.rxjava3.core.Single
import org.junit.Test

class ProductRepositoryTest {

    private val productId = "productId"
    private val productTitle = "product title"
    private val productPrice = 9.99
    private val productImageUrl = "https://image.url/1.jpg"

    private val expectedProducts = listOf(Product(productId, productTitle, productPrice, productImageUrl))

    private val response = ApiProductResponse(
        listOf(
            ApiProduct(
                id = productId,
                title = productTitle,
                list_price =  productPrice,
                images = ApiProductImageSize(ApiProductImage(src = productImageUrl))
            )
        )
    )
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