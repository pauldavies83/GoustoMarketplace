package dev.pauldavies.goustomarketplace

import android.app.Application
import android.content.res.Resources
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltAndroidApp
class GoustoMarketplaceApplication : Application(), ImageLoaderFactory {

    @Inject lateinit var imageLoader: ImageLoader

    override fun newImageLoader() = imageLoader
}

val Int.dp: Int
    get() {
        val density = Resources.getSystem().displayMetrics.density
        return (this * density).roundToInt()
    }