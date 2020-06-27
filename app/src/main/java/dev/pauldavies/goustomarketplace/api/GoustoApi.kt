package dev.pauldavies.goustomarketplace.api

import io.reactivex.rxjava3.core.Single
import kotlinx.serialization.Serializable
import retrofit2.http.GET

interface GoustoApi {

    @GET("products")
    fun getProducts(): Single<ApiProductResponse>
}

@Serializable
data class ApiProductResponse(val data: List<ApiProduct>)

@Serializable
data class ApiProduct(
    val id: String,
    val title: String,
    val list_price: Double
)