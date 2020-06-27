package dev.pauldavies.goustomarketplace.productlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import dev.pauldavies.goustomarketplace.R
import kotlinx.android.synthetic.main.fragment_product_list.*

@AndroidEntryPoint
class ProductListFragment : Fragment(R.layout.fragment_product_list) {

    private val viewModel: ProductListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, Observer {  state ->
            when (state) {
                is ProductListViewModel.State.Loading -> {}
                is ProductListViewModel.State.Loaded -> {
                    title.text = state.title
                }
            }
        })
    }

}