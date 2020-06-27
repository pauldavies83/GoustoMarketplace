package dev.pauldavies.goustomarketplace.repository

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stub
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.pauldavies.goustomarketplace.api.*
import dev.pauldavies.goustomarketplace.persistence.ProductsStorage
import dev.pauldavies.goustomarketplace.persistence.model.Product
import io.reactivex.Observable
import io.reactivex.Single
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

    private val productsStorage = mock<ProductsStorage> {
        whenever(it.getAllProducts()).thenReturn(Observable.just(expectedProducts))
    }

    private val repository by lazy { ProductRepository(goustoApi, productsStorage) }

    @Test
    fun `database response returned as queried`() {
        repository.products().test()
            .assertResult(expectedProducts)
    }

    @Test
    fun `when api sync succeeds, insert values into database`() {
        repository.syncProducts().test()

        verify(productsStorage).insertProducts(expectedProducts)
    }

    @Test
    fun `when api sync fails, database response returned as queried`() {
        goustoApi.stub {
            whenever(it.getProducts()).thenReturn(Single.error(Throwable()))
        }
        repository.syncProducts()

        repository.products().test()
            .assertResult(expectedProducts)
    }
}