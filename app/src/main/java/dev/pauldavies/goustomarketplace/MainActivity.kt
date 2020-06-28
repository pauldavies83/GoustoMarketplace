package dev.pauldavies.goustomarketplace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import dev.pauldavies.goustomarketplace.productlist.ProductListFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(android.R.id.content, ProductListFragment())
            }
        }
    }
}

fun Fragment.requireAppCompatActivity() = activity as AppCompatActivity