package dev.pauldavies.goustomarketplace.repository

import com.nhaarman.mockitokotlin2.*
import dev.pauldavies.goustomarketplace.api.*
import dev.pauldavies.goustomarketplace.persistence.ProductsStorage
import dev.pauldavies.goustomarketplace.persistence.model.DbCategory
import dev.pauldavies.goustomarketplace.persistence.model.DbProduct
import dev.pauldavies.goustomarketplace.persistence.model.DbProductWithCategories
import dev.pauldavies.goustomarketplace.persistence.model.DbProductWithCategoriesCrossRef
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test

class ProductRepositoryTest {

    private val productId = "productId"
    private val productTitle = "product title"
    private val productPrice = 9.99
    private val productImageUrl = "https://image.url/1.jpg"
    private val productAgeRestricted = true
    private val categoryId1 = "id1"
    private val categoryId2 = "id2"

    private val categoryTitle1 = "title1"
    private val categoryTitle2 = "title2"

    private val dbCategories = listOf(
        DbCategory(categoryId1, categoryTitle1),
        DbCategory(categoryId2, categoryTitle2)
    )
    private val dbProducts = listOf(DbProduct(productId, productTitle, productPrice, productImageUrl, productAgeRestricted))

    private val dDbProductsWithCategories = listOf(
        DbProductWithCategories(
            dbProducts[0],
            dbCategories
        )
    )
    private val products = listOf(
        Product(productId, productTitle, productPrice, productImageUrl, productAgeRestricted, listOf(categoryTitle1, categoryTitle2))
    )

    private val productXCategories = listOf(
        DbProductWithCategoriesCrossRef(productId, categoryId1),
        DbProductWithCategoriesCrossRef(productId, categoryId2)
    )

    private val response = ApiProductResponse(
        listOf(
            ApiProduct(
                id = productId,
                title = productTitle,
                list_price =  productPrice,
                images = ApiProductImageSize(ApiProductImage(src = productImageUrl)),
                age_restricted = productAgeRestricted,
                categories = listOf(
                    ApiCategory(categoryId1, categoryTitle1),
                    ApiCategory(categoryId2, categoryTitle2)
                )
            )
        )
    )
    private val goustoApi = mock<GoustoApi> {
        whenever(it.getProducts()).thenReturn(Single.just(response))
    }

    private val productsStorage = mock<ProductsStorage> {
        whenever(it.getProductsWithCategories(any())).thenReturn(Observable.just(dDbProductsWithCategories))
    }

    private val repository by lazy { ProductRepository(goustoApi, productsStorage) }

    @Test
    fun `database response returned as queried`() {
        repository.products().test()
            .assertResult(products)
    }

    @Test
    fun `when api sync succeeds, insert values into database`() {
        repository.syncProducts().test()

        verify(productsStorage).insertProductsWithCategories(
            dbProducts,
            dbCategories,
            productXCategories
        )
    }

    @Test
    fun `when api sync fails, database response returned as queried`() {
        goustoApi.stub {
            whenever(it.getProducts()).thenReturn(Single.error(Throwable()))
        }
        repository.syncProducts()

        repository.products().test()
            .assertResult(products)
    }
}