package dev.pauldavies.goustomarketplace.api

import io.reactivex.Single
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET

interface GoustoApi {

    @GET("products?image_sizes[]=750")
    fun getProducts(): Single<ApiProductResponse>
}

@Serializable
data class ApiProductResponse(val data: List<ApiProduct>)

@Serializable
data class ApiProduct(
    val id: String,
    val title: String,
    val list_price: Double,
    val images: ApiProductImageSize
)

@Serializable
data class ApiProductImageSize(
    @SerialName(value = "750") val size: ApiProductImage? = null
)

@Serializable
data class ApiProductImage(val src: String)