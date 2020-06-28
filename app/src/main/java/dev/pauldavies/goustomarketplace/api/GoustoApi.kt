package dev.pauldavies.goustomarketplace.api

import io.reactivex.Single
import kotlinx.serialization.*
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.content
import retrofit2.http.GET
import retrofit2.http.Query

interface GoustoApi {

    @GET("products?includes[]=categories")
    fun getProducts(@Query(value = "image_sizes[]") imageWidth: Int): Single<ApiProductResponse>
}

@Serializable
data class ApiProductResponse(val data: List<ApiProduct>)

@Serializable
data class ApiProduct(
    val id: String,
    val title: String,
    val description: String,
    val list_price: Double,
    val categories: List<ApiCategory> = emptyList(),
    val images: ApiProductImages,
    val age_restricted: Boolean
)

@Serializable
data class ApiCategory(val id: String, val title: String)

@Serializable
data class ApiProductImages(val imageSizeUrls: List<String>) {
    @Serializer(forClass = ApiProductImages::class)
    companion object : KSerializer<ApiProductImages> {
        override fun deserialize(decoder: Decoder): ApiProductImages {
            val input = decoder as? JsonInput
                ?: throw SerializationException("Expected JsonInput for ${decoder::class}")
            val jsonObject = input.decodeJson() as? JsonObject
                ?: throw SerializationException("Expected JsonObject for ${input.decodeJson()::class}")

            val images = jsonObject.mapNotNull {
                if (it.value == JsonNull) null
                else {
                    it.value.jsonObject["src"]?.content
                }
            }
            return ApiProductImages(images.toList())
        }
    }
}