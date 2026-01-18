package com.kotlinpractice.blinkitclone.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlinpractice.blinkitclone.R
import com.kotlinpractice.blinkitclone.data.model.Product
import com.kotlinpractice.blinkitclone.databinding.ItemProductBinding

class HomeAdapter :
    ListAdapter<Product, HomeAdapter.ProductViewHolder>(DiffCallback()) {

    inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                tvName.text = product.title
                tvPrice.text = "₹${product.price}"
                Glide.with(imgProduct.context)
                    .load(product.thumbnail)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_background)
                    .into(imgProduct)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(old: Product, new: Product) =
            old.id == new.id

        override fun areContentsTheSame(old: Product, new: Product) =
            old == new
    }
}
