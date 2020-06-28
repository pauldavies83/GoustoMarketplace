package dev.pauldavies.goustomarketplace.productlist

import android.graphics.Rect
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import dev.pauldavies.goustomarketplace.R
import dev.pauldavies.goustomarketplace.dp
import kotlinx.android.synthetic.main.fragment_product_list.*

@AndroidEntryPoint
class ProductListFragment : Fragment(R.layout.fragment_product_list) {

    private val viewModel: ProductListViewModel by viewModels()
    private val productListAdapter = ProductListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_product_list, menu)

        val searchView = menu.findItem(R.id.productListSearch).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onQueryChanged(newText)
                return true
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(productListToolbar)

        productListRecyclerView.apply {
            adapter = productListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(VerticalSpaceItemDecoration(8.dp))
        }

        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            productListLoadingProgress.isVisible = state is ProductListViewModel.State.Loading
            productListNoResultsLabel.isVisible = state is ProductListViewModel.State.NoResults
            productListRecyclerView.isVisible = state is ProductListViewModel.State.Loaded

            if (state is ProductListViewModel.State.Loaded) {
                productListAdapter.submitList(state.products)
            }
        })
    }

}

internal class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) : ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = verticalSpaceHeight
    }
}