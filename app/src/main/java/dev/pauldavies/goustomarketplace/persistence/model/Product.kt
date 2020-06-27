package dev.pauldavies.goustomarketplace.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    @PrimaryKey val id: String,
    val title: String,
    val price: Double,
    val imageUrl: String?
)