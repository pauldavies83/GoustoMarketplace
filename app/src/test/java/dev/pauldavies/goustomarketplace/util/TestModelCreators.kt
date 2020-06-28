package dev.pauldavies.goustomarketplace.util

import dev.pauldavies.goustomarketplace.api.ApiCategory
import dev.pauldavies.goustomarketplace.api.ApiProduct
import dev.pauldavies.goustomarketplace.api.ApiProductImages
import dev.pauldavies.goustomarketplace.persistence.model.DbCategory
import dev.pauldavies.goustomarketplace.persistence.model.DbProduct
import dev.pauldavies.goustomarketplace.persistence.model.DbProductWithCategories
import dev.pauldavies.goustomarketplace.repository.Product
import java.util.*

fun randomString() = UUID.randomUUID().toString()
fun randomDouble() = Random().nextDouble()
fun randomInt() = Random().nextInt()

object ProductCreator {
    private val productId = randomString()
    private val productTitle = randomString()
    private val productDescription = randomString()
    private val productImageUrl = randomString()
    private val productPrice = randomDouble()
    private const val productAgeRestricted = true
    private val apiCategories = listOf(
        ApiCategory(id = randomString(), title = randomString()),
        ApiCategory(id = randomString(), title = randomString())
    )

    fun apiProduct(): ApiProduct {
        return ApiProduct(
            id = productId,
            title = productTitle,
            description = productDescription,
            list_price = productPrice,
            images = ApiProductImages(listOf(productImageUrl)),
            categories = apiCategories,
            age_restricted = productAgeRestricted
        )
    }

    fun dbProductWithCategories(apiProduct: ApiProduct = apiProduct()): DbProductWithCategories {
        return DbProductWithCategories(
            product = DbProduct(
                id = apiProduct.id,
                title = apiProduct.title,
                description = apiProduct.description,
                price = apiProduct.list_price,
                imageUrl = apiProduct.images.imageSizeUrls.firstOrNull(),
                ageRestricted = apiProduct.age_restricted
            ),
            categories = apiProduct.categories.map { DbCategory(it.id, it.title) }
        )
    }

    fun product(dbProductWithCategories: DbProductWithCategories = dbProductWithCategories()): Product {
        return Product(
            id = dbProductWithCategories.product.id,
            title = dbProductWithCategories.product.title,
            description = dbProductWithCategories.product.description,
            price = dbProductWithCategories.product.price,
            imageUrl = dbProductWithCategories.product.imageUrl,
            ageRestricted = dbProductWithCategories.product.ageRestricted,
            categories = dbProductWithCategories.categories.map { it.title }
        )
    }
}
