package com.kotlinpractice.blinkitclone.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kotlinpractice.blinkitclone.R
import com.kotlinpractice.blinkitclone.data.model.Category
import com.kotlinpractice.blinkitclone.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val onCategoryClick: (Category) -> Unit
) : ListAdapter<Category, CategoryAdapter.VH>(Diff) {

    inner class VH(val binding: ItemCategoryBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val category = getItem(position)

        holder.binding.apply {
            val context = root.context

            tvCategory.text = category.name

            root.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    if (category.isSelected)
                        R.color.category_bg_selected
                    else
                        R.color.category_bg_unselected
                )
            )

            root.strokeColor = ContextCompat.getColor(
                context,
                if (category.isSelected)
                    R.color.category_stroke_selected
                else
                    R.color.category_stroke_unselected
            )

            tvCategory.setTextColor(
                ContextCompat.getColor(
                    context,
                    if (category.isSelected)
                        R.color.category_text_selected
                    else
                        R.color.category_text_unselected
                )
            )

            root.setOnClickListener {
                onCategoryClick(category)
            }
        }
    }


    companion object {
        val Diff = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(a: Category, b: Category) = a.slug == b.slug
            override fun areContentsTheSame(a: Category, b: Category) = a == b
        }
    }
}
