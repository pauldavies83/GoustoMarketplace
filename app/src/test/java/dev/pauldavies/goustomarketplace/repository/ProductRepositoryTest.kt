package dev.pauldavies.goustomarketplace.repository

import com.nhaarman.mockitokotlin2.*
import dev.pauldavies.goustomarketplace.api.ApiProductResponse
import dev.pauldavies.goustomarketplace.api.GoustoApi
import dev.pauldavies.goustomarketplace.base.DeviceMetrics
import dev.pauldavies.goustomarketplace.persistence.ProductsStorage
import dev.pauldavies.goustomarketplace.persistence.model.DbProductWithCategoriesCrossRef
import dev.pauldavies.goustomarketplace.util.ProductCreator
import dev.pauldavies.goustomarketplace.util.randomInt
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test

class ProductRepositoryTest {

    private val apiProduct = ProductCreator.apiProduct()
    private val dbProductWithCategories = ProductCreator.dbProductWithCategories(apiProduct)
    private val product = ProductCreator.product(dbProductWithCategories)

    private val productXCategories = listOf(
        DbProductWithCategoriesCrossRef(dbProductWithCategories.product.id, dbProductWithCategories.categories[0].id),
        DbProductWithCategoriesCrossRef(dbProductWithCategories.product.id, dbProductWithCategories.categories[1].id)
    )

    private val deviceMetrics = mock<DeviceMetrics>() {
        whenever(it.screenSize()).thenReturn(DeviceMetrics.Size(randomInt(), randomInt()))
    }

    private val response = ApiProductResponse(listOf(apiProduct))
    private val goustoApi = mock<GoustoApi> {
        whenever(it.getProducts(deviceMetrics.screenSize().x)).thenReturn(Single.just(response))
    }

    private val productsStorage = mock<ProductsStorage> {
        whenever(it.getProductsWithCategories(any())).thenReturn(Observable.just(listOf(dbProductWithCategories)))
        whenever(it.getProductWithCategories(any())).thenReturn(Single.just(dbProductWithCategories))
    }

    private val repository by lazy {
        ProductRepository(goustoApi, productsStorage, deviceMetrics)
    }

    @Test
    fun `products database response returned as queried`() {
        repository.products().test()
            .assertResult(listOf(product))
    }

    @Test
    fun `product database response returned as queried`() {
        repository.product(product.id).test()
            .assertResult(product)
    }

    @Test
    fun `screen width passed to api as requested image width`() {
        repository.syncProducts().test()

        verify(goustoApi).getProducts(deviceMetrics.screenSize().x)
    }

    @Test
    fun `when api sync succeeds, insert values into database`() {
        repository.syncProducts().test()

        verify(productsStorage).insertProductsWithCategories(listOf(dbProductWithCategories.product), dbProductWithCategories.categories, productXCategories)
    }

    @Test
    fun `when api sync fails, database response returned as queried`() {
        goustoApi.stub {
            whenever(it.getProducts(any())).thenReturn(Single.error(Throwable()))
        }
        repository.syncProducts()

        repository.products().test()
            .assertResult(listOf(product))
    }
}