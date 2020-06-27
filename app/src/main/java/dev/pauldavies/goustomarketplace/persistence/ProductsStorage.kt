package dev.pauldavies.goustomarketplace.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.pauldavies.goustomarketplace.persistence.model.Product
import io.reactivex.Observable

@Dao
interface ProductsStorage {

    @Query("SELECT * FROM product")
    fun getAllProducts(): Observable<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(it: List<Product>)

}