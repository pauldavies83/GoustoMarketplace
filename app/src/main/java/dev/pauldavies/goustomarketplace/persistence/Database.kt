package dev.pauldavies.goustomarketplace.persistence

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.pauldavies.goustomarketplace.persistence.model.DbCategory
import dev.pauldavies.goustomarketplace.persistence.model.DbProduct
import dev.pauldavies.goustomarketplace.persistence.model.DbProductWithCategoriesCrossRef

@androidx.room.Database(
    entities = [
        DbProduct::class,
        DbCategory::class,
        DbProductWithCategoriesCrossRef::class
    ],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun productsStorage(): ProductsStorage

    companion object {
        fun newInstance(context: Context) = Room.databaseBuilder(
            context,
            Database::class.java, "gousto-marketplace-room-db"
        ).build()
    }
}