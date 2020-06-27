package dev.pauldavies.goustomarketplace.repository

import javax.inject.Inject

internal class ProductRepository @Inject constructor() {

    fun products(): List<Product> {
        return listOf(Product(title = "Hello Gousto"))
    }
}

data class Product(val title: String)