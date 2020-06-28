package dev.pauldavies.goustomarketplace.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.api.load
import dagger.hilt.android.AndroidEntryPoint
import dev.pauldavies.goustomarketplace.R
import dev.pauldavies.goustomarketplace.base.emptyString
import kotlinx.android.synthetic.main.fragment_product_details.*

@AndroidEntryPoint
class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {

    private fun Fragment.configureToolbar(title: String = emptyString(), setDisplayAsHomeUpEnabled: Boolean = false) {
        val toolbar = view?.findViewById<Toolbar>(R.id.toolbar) ?: return
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(setDisplayAsHomeUpEnabled)
                it.title = title
            }
        }
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
    }

    private val viewModel: ProductDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureToolbar(setDisplayAsHomeUpEnabled = true)

        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            productDetailsError.isVisible = state is ProductDetailsViewModel.State.Error
            when (state) {
                is ProductDetailsViewModel.State.Loaded -> {
                    with (state) {
                        productDetailsImage.apply {
                            if (imageUrl != null) {
                                load(imageUrl)
                            } else {
                                isVisible = false
                            }
                            productDetailsTitle.text = title
                            producDetailstAgeRestricted.isVisible = ageRestricted
                            productDetailsDescription.text = description
                            productDetailsPrice.text = price
                            categories.forEach { categoryTitle ->
                                val textView = LayoutInflater.from(context).inflate(
                                    R.layout.item_product_details_category,
                                    productDetailsCategoriesContainer,
                                    false
                                ) as TextView
                                textView.text = categoryTitle
                                productDetailsCategoriesContainer.addView(textView)
                            }
                        }
                    }
                }
            }
        })
    }

    companion object {
        fun newInstance(productId: String) = ProductDetailsFragment().apply {
            this.arguments = bundleOf(Pair(PRODUCT_ID_KEY, productId))
        }
    }
}