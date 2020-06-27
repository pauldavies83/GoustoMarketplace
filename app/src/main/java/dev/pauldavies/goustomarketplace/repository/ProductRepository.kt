package dev.pauldavies.goustomarketplace.repository

import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class ProductRepository @Inject constructor() {

    fun products(): Observable<List<Product>> {
        return Observable.just(
            listOf(Product(title = "Hello World"))
        )
    }
}

data class Product(val title: String)