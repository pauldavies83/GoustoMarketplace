package dev.pauldavies.goustomarketplace.persistence.model

import androidx.room.*

@Entity(tableName = "product")
data class DbProduct(
    @PrimaryKey @ColumnInfo(name = "productId") val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val imageUrl: String?,
    val ageRestricted: Boolean
)

@Entity(tableName = "category")
data class DbCategory(
    @PrimaryKey @ColumnInfo(name = "categoryId") val id: String,
    val title: String
)

@Entity(tableName = "productXcateogies", primaryKeys = ["productId", "categoryId"])
data class DbProductWithCategoriesCrossRef(
    val productId: String,
    val categoryId: String
)

data class DbProductWithCategories(
    @Embedded val product: DbProduct,
    @Relation(
        parentColumn = "productId",
        entityColumn = "categoryId",
        associateBy = Junction(DbProductWithCategoriesCrossRef::class)
    ) val categories: List<DbCategory>
)