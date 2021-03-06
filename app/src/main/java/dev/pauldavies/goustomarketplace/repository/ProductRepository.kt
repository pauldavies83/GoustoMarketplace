package dev.pauldavies.goustomarketplace.repository

import dev.pauldavies.goustomarketplace.api.ApiCategory
import dev.pauldavies.goustomarketplace.api.ApiProduct
import dev.pauldavies.goustomarketplace.api.GoustoApi
import dev.pauldavies.goustomarketplace.base.DeviceMetrics
import dev.pauldavies.goustomarketplace.base.emptyString
import dev.pauldavies.goustomarketplace.persistence.ProductsStorage
import dev.pauldavies.goustomarketplace.persistence.model.DbCategory
import dev.pauldavies.goustomarketplace.persistence.model.DbProduct
import dev.pauldavies.goustomarketplace.persistence.model.DbProductWithCategories
import dev.pauldavies.goustomarketplace.persistence.model.DbProductWithCategoriesCrossRef
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

internal class ProductRepository @Inject constructor(
    private val goustoApi: GoustoApi,
    private val productsStorage: ProductsStorage,
    private val deviceMetrics: DeviceMetrics
) {

    fun product(productId: String): Single<Product> {
        return productsStorage.getProductWithCategories(productId).map {
            it.toDomainProduct()
        }
    }

    fun products(queryTitle: String = emptyString()): Observable<List<Product>> {
        return productsStorage.getProductsWithCategories("%$queryTitle%")
            .map { productsWithCategories ->
                productsWithCategories.map { it.toDomainProduct() }
            }
    }

    fun syncProducts(): Completable {
        return goustoApi.getProducts(imageWidth = deviceMetrics.screenSize().x)
            .doOnSuccess { apiResponse ->
                insertApiResponseToStorage(apiResponse.data)
            }.ignoreElement()
    }

    private fun insertApiResponseToStorage(apiProducts: List<ApiProduct>) {
        val products = mutableSetOf<DbProduct>()
        val categories = mutableSetOf<DbCategory>()
        val productXcategory = mutableSetOf<DbProductWithCategoriesCrossRef>()

        apiProducts.forEach { apiProduct ->
            val dbProduct = apiProduct.toDbProduct()
            products.add(dbProduct)

            apiProduct.categories.forEach { apiCategory ->
                val dbCategory = apiCategory.toDbCategory()

                categories.add(dbCategory)
                productXcategory.add(DbProductWithCategoriesCrossRef(dbProduct.id, dbCategory.id))
            }
        }
        productsStorage.insertProductsWithCategories(
            products = products.toList(),
            categories = categories.toList(),
            productXcategory = productXcategory.toList()
        )
    }
}

private fun ApiProduct.toDbProduct() = DbProduct(
    id = id,
    title = title,
    description = description,
    price = list_price,
    imageUrl = images.imageSizeUrls.firstOrNull(),
    ageRestricted = age_restricted
)

private fun ApiCategory.toDbCategory() = DbCategory(
    id = id,
    title = title
)

private fun DbProductWithCategories.toDomainProduct() = Product(
    id = product.id,
    title = product.title,
    description = product.description,
    price = product.price,
    imageUrl = product.imageUrl,
    ageRestricted = product.ageRestricted,
    categories = categories.map { it.title }
)

data class Product(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val imageUrl: String?,
    val ageRestricted: Boolean,
    val categories: List<String>
)