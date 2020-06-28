package dev.pauldavies.goustomarketplace.productdetails

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import dev.pauldavies.goustomarketplace.R
import kotlinx.android.synthetic.main.fragment_product_details.*

@AndroidEntryPoint
class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {

    private val viewModel: ProductDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ProductDetailsViewModel.State.Loaded -> productDetailsTitle.text = state.title
            }
        })
    }

    companion object {
        fun newInstance(productId: String) = ProductDetailsFragment().apply {
            this.arguments = bundleOf(Pair(PRODUCT_ID_KEY, productId))
        }
    }
}