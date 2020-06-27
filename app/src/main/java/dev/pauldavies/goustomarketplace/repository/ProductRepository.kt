package dev.pauldavies.goustomarketplace.repository

import dev.pauldavies.goustomarketplace.api.GoustoApi
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class ProductRepository @Inject constructor(
    private val goustoApi: GoustoApi
) {

    fun products(): Single<List<Product>> {
        return goustoApi.getProducts().map { apiResponse ->
            apiResponse.data.map { Product(it.id, it.title, it.list_price, it.images.size?.src ) }
        }
    }
}

data class Product(val id: String, val title: String, val price: Double, val imageUrl: String?)