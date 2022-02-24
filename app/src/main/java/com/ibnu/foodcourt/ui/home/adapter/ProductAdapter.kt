package com.ibnu.foodcourt.ui.home.adapter

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibnu.foodcourt.R
import com.ibnu.foodcourt.data.model.Product
import com.ibnu.foodcourt.databinding.ItemMenuBinding
import com.ibnu.foodcourt.utils.ConstVal.BASE_IMAGE_URL
import com.ibnu.foodcourt.utils.ext.popTap
import com.ibnu.foodcourt.utils.ext.toRupiah

class ProductAdapter(private val itemHandler: ProductItemHandler) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var listProduct = ArrayList<Product>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listProduct: List<Product>) {
        this.listProduct.clear()
        this.listProduct.addAll(listProduct)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = listProduct[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = listProduct.size

    inner class ProductViewHolder(private val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {

            binding.btnAddToCart.setOnClickListener {
                it.popTap()
                Handler(Looper.getMainLooper()).postDelayed({
                    itemHandler.addToCart(
                        product,
                        product.price
                    )
                }, 200)
            }

            binding.txvMenuName.text = product.productName
            binding.txvMenuPrice.text = product.price.toRupiah()

            Glide.with(binding.root.context)
                .load("${BASE_IMAGE_URL}${product.thumbnail}")
                .placeholder(R.color.input_color)
                .into(binding.imgMenu)
        }
    }
}