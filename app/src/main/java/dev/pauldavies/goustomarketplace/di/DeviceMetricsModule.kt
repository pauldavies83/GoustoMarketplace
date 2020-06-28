package dev.pauldavies.goustomarketplace.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.pauldavies.goustomarketplace.base.AndroidDeviceMetrics
import dev.pauldavies.goustomarketplace.base.DeviceMetrics

@Module
@InstallIn(ApplicationComponent::class)
object DeviceMetricsModule {
    @Provides
    fun screenSize(@ApplicationContext context: Context): DeviceMetrics = AndroidDeviceMetrics(context)
}