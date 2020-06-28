package dev.pauldavies.goustomarketplace.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.pauldavies.goustomarketplace.base.AndroidLogger
import dev.pauldavies.goustomarketplace.base.Logger

@Module
@InstallIn(ApplicationComponent::class)
object LoggingModule {
    @Provides fun logger(): Logger = AndroidLogger()
}