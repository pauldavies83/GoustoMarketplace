package dev.pauldavies.goustomarketplace.repository

import dev.pauldavies.goustomarketplace.api.ApiProduct
import dev.pauldavies.goustomarketplace.api.GoustoApi
import dev.pauldavies.goustomarketplace.persistence.ProductsStorage
import dev.pauldavies.goustomarketplace.persistence.model.Product
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

internal class ProductRepository @Inject constructor(
    private val goustoApi: GoustoApi,
    private val productsStorage: ProductsStorage
) {

    fun syncProducts(): Completable {
        return goustoApi.getProducts()
            .map { apiResponse ->
                apiResponse.data.map { it.toProduct() }
            }.doOnSuccess {
                productsStorage.insertProducts(it)
            }.ignoreElement()
    }

    fun products(): Observable<List<Product>> {
        return productsStorage.getAllProducts()
    }
}

private fun ApiProduct.toProduct() = Product(
    id = id,
    title = title,
    price = list_price,
    imageUrl = images.size?.src
)