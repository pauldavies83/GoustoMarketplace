package dev.pauldavies.goustomarketplace.productlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import dev.pauldavies.goustomarketplace.R
import kotlinx.android.synthetic.main.item_product_list.view.*

val itemCallback = object : DiffUtil.ItemCallback<ProductListItem>() {
    override fun areItemsTheSame(old: ProductListItem, new: ProductListItem) = old.id == new.id
    override fun areContentsTheSame(old: ProductListItem, new: ProductListItem) = old == new
}

internal class ProductListAdapter :
    ListAdapter<ProductListItem, ProductListAdapter.ProductViewHolder>(itemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_product_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(product: ProductListItem) {
            itemView.apply {
                with (product) {
                    productTitle.text = title
                    productPrice.text = price
                    productImage.apply {
                        clipToOutline = true
                        if (imageUrl != null) {
                            load(imageUrl)
                        } else {
                            scaleType = ImageView.ScaleType.CENTER_INSIDE
                            setImageResource(R.mipmap.ic_launcher_round)
                        }
                    }
                    productAgeRestricted.isVisible = ageRestricted
                }
            }
        }
    }

}

data class ProductListItem(
    val id: String,
    val title: String,
    val price: String,
    val imageUrl: String?,
    val ageRestricted: Boolean
)