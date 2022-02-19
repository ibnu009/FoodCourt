package com.ibnu.foodcourt.ui.home.adapter

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibnu.foodcourt.R
import com.ibnu.foodcourt.data.model.Category
import com.ibnu.foodcourt.databinding.ItemCategoryBinding
import com.ibnu.foodcourt.utils.ext.popTap

class CategoryAdapter(private val itemHandler: CategoryItemHandler) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val listCategory = ArrayList<Category>()
    private var selectedPos = 0


    @SuppressLint("NotifyDataSetChanged")
    fun setData(listCategory: List<Category>) {
        this.listCategory.addAll(listCategory)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = listCategory[position]
        holder.bind(category, position)
        holder.itemView.setOnClickListener {
            it.popTap()
            notifyItemChanged(selectedPos)
            selectedPos = holder.layoutPosition
            notifyItemChanged(selectedPos)
            Handler(Looper.getMainLooper()).postDelayed({
                itemHandler.onCategoryItemClicked(
                    category.id
                )
            }, 200)
        }
    }

    override fun getItemCount(): Int = listCategory.size

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category, position: Int) {

            if (selectedPos == position){
                binding.txvCategoryName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey_800))
                binding.cardContainer.background = ContextCompat.getDrawable(binding.root.context, R.color.grey_800)
            } else {
                binding.txvCategoryName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey_400))
                binding.cardContainer.background = ContextCompat.getDrawable(binding.root.context, R.color.grey_400)
            }

            binding.txvCategoryName.text = category.categoryName

            val categoryIcon = getProperIconFromCategoryName(category.categoryName)
            binding.imgCategory.setImageDrawable(ContextCompat.getDrawable(binding.root.context, categoryIcon))
        }
    }

    private fun getProperIconFromCategoryName(name: String): Int {
        return when {
            name.equals("Makanan", true) || name.equals("Food", true) -> R.drawable.ic_food
            name.equals("Minuman", true) || name.equals("Drink", true) -> R.drawable.ic_drink
            name.equals("Cemilan", true) || name.equals("Snack", true) -> R.drawable.ic_snack
            name.equals("Bakery", true) || name.equals("Roti", true) -> R.drawable.ic_bakery
            else -> R.drawable.ic_food
        }
    }
}