package dev.pauldavies.goustomarketplace.di

import android.content.Context
import coil.ImageLoader
import coil.util.CoilUtils
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.pauldavies.goustomarketplace.R
import dev.pauldavies.goustomarketplace.api.GoustoApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
open class NetworkModule {

    open fun baseUrl() = "https://api.gousto.co.uk/products/v2.0/"

    @Provides
    @Singleton
    fun providesOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .cache(CoilUtils.createDefaultCache(context))
            .build()
    }

    @Provides
    @Singleton
    fun providesImageLoader(@ApplicationContext context: Context, okHttpClient: OkHttpClient): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .fallback(R.drawable.ic_placeholder)
            .okHttpClient(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesGoustoApi(okHttpClient: OkHttpClient): GoustoApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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