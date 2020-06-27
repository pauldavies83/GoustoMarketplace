package dev.pauldavies.goustomarketplace.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.pauldavies.goustomarketplace.api.GoustoApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
open class NetworkModule {

    open fun baseUrl() = "https://api.gousto.co.uk/products/v2.0/"

    @Provides
    @Singleton
    fun providesGoustoApi(): GoustoApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(
                /** enable ignoreUnknownKeys to save having to parse every json key from the API &
                    enable isLenient as inconsistent quote usage in api response,
                    this should not be considered production ready!
                 **/
                Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true, isLenient = true))
                    .asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(GoustoApi::class.java)
    }


}