package dev.pauldavies.goustomarketplace.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.pauldavies.goustomarketplace.persistence.Database

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Provides
    fun providesRoomDatabase(@ApplicationContext context: Context) = Database.newInstance(context)

    @Provides
    fun providesProducstStorage(database: Database) = database.productsStorage()

}