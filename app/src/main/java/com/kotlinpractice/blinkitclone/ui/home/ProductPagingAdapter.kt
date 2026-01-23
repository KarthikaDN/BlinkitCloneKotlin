package com.kotlinpractice.blinkitclone.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlinpractice.blinkitclone.R
import com.kotlinpractice.blinkitclone.data.model.Product
import com.kotlinpractice.blinkitclone.databinding.ItemProductBinding

class ProductPagingAdapter :
    PagingDataAdapter<Product, ProductPagingAdapter.VH>(Diff) {

    inner class VH(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VH {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val product = getItem(position) ?: return

        holder.binding.apply {
            tvName.text = product.title
            tvPrice.text = "₹${product.price}"

            Glide.with(imgProduct)
                .load(product.thumbnail)
                .into(imgProduct)
        }
    }

    companion object {
        val Diff = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(old: Product, new: Product): Boolean =
                old.id == new.id

            override fun areContentsTheSame(old: Product, new: Product): Boolean =
                old == new
        }
    }
}


