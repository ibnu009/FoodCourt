package com.ibnu.foodcourt.ui.cart.adapter

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibnu.foodcourt.R
import com.ibnu.foodcourt.data.model.Order
import com.ibnu.foodcourt.databinding.ItemOrderBinding
import com.ibnu.foodcourt.utils.ConstVal
import com.ibnu.foodcourt.utils.UiConstValue.FAST_ANIMATION_TIME
import com.ibnu.foodcourt.utils.ext.popTap
import com.ibnu.foodcourt.utils.ext.toRupiah

class OrderAdapter(private val itemHandler: OrderItemHandler) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private var listOrder = ArrayList<Order>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listOrder: List<Order>) {
        this.listOrder.clear()
        this.listOrder.addAll(listOrder)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        listOrder.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listOrder.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = listOrder[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int = listOrder.size

    inner class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {

            binding.btnIncrementQuantity.setOnClickListener {
                it.popTap()
                Handler(Looper.getMainLooper()).postDelayed({
                    order.quantity++
                    binding.txvCartQuantity.text = order.quantity.toString()
                    itemHandler.onIncrementOrderQuantity(order, layoutPosition)
                }, FAST_ANIMATION_TIME)
            }

            binding.btnDecrementQuantity.setOnClickListener {
                it.popTap()
                Handler(Looper.getMainLooper()).postDelayed({
                    if (order.quantity == 1) {
                        itemHandler.onDeleteOrder(order, layoutPosition)
                    } else {
                        order.quantity--
                        binding.txvCartQuantity.text = order.quantity.toString()
                        itemHandler.onDecrementOrderQuantity(order, layoutPosition)
                    }
                }, FAST_ANIMATION_TIME)
            }

            binding.btnRemoveFromOrder.setOnClickListener {
                it.popTap()
                Handler(Looper.getMainLooper()).postDelayed({
                    itemHandler.onDeleteOrder(order, layoutPosition)
                }, FAST_ANIMATION_TIME)
            }

            binding.txvCartQuantity.text = order.quantity.toString()
            binding.txvOrderName.text = order.productName
            binding.txvOrderPrice.text = order.price.toRupiah()

            Glide.with(binding.root.context)
                .load("${ConstVal.BASE_IMAGE_URL}${order.thumbnail}")
                .placeholder(R.color.input_color)
                .into(binding.imvOrder)
        }
    }
}