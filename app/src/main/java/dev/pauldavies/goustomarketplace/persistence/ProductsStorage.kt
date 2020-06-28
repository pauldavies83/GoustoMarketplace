package dev.pauldavies.goustomarketplace.persistence

import androidx.room.*
import dev.pauldavies.goustomarketplace.persistence.model.DbCategory
import dev.pauldavies.goustomarketplace.persistence.model.DbProduct
import dev.pauldavies.goustomarketplace.persistence.model.DbProductWithCategories
import dev.pauldavies.goustomarketplace.persistence.model.DbProductWithCategoriesCrossRef
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface ProductsStorage {

    @Query("SELECT * FROM product")
    fun getAllProducts(): Observable<List<DbProduct>>

    @Transaction
    @Query("SELECT * FROM product WHERE title LIKE :queryTitle")
    fun getProductsWithCategories(queryTitle: String): Observable<List<DbProductWithCategories>>

    @Transaction
    @Query("SELECT * FROM product WHERE productId = :productId")
    fun getProductWithCategories(productId: String): Single<DbProductWithCategories>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(products: List<DbProduct>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategories(categories: List<DbCategory>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductXCategory(productXCategory: List<DbProductWithCategoriesCrossRef>)

    @Transaction
    fun insertProductsWithCategories(
        products: List<DbProduct>,
        categories: List<DbCategory>,
        productXcategory: List<DbProductWithCategoriesCrossRef>
    ) {
        insertProducts(products)
        insertCategories(categories)
        insertProductXCategory(productXcategory)
    }
}