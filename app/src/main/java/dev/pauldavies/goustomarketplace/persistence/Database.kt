package dev.pauldavies.goustomarketplace.persistence

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.pauldavies.goustomarketplace.persistence.model.Product

@androidx.room.Database(entities = [Product::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun productsStorage(): ProductsStorage

    companion object {
        fun newInstance(context: Context) = Room.databaseBuilder(
            context,
            Database::class.java, "gousto-marketplace-room-db"
        ).build()
    }
}