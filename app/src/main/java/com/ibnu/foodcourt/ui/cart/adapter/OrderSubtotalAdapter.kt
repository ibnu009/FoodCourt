package com.ibnu.foodcourt.ui.cart.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibnu.foodcourt.data.model.Order
import com.ibnu.foodcourt.databinding.ItemSubTotalBinding
import com.ibnu.foodcourt.utils.ext.toRupiah

class OrderSubtotalAdapter :
    RecyclerView.Adapter<OrderSubtotalAdapter.OrderSubtotalViewHolder>() {

    private var listOrder = ArrayList<Order>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listOrder: List<Order>) {
        this.listOrder.clear()
        this.listOrder.addAll(listOrder)
        notifyDataSetChanged()
    }

    fun changeData(order: Order, position: Int) {
        listOrder[position] = order
        notifyItemChanged(position)
    }

    fun removeItem(position: Int) {
        listOrder.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listOrder.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderSubtotalViewHolder {
        val binding =
            ItemSubTotalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderSubtotalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderSubtotalViewHolder, position: Int) {
        val order = listOrder[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int = listOrder.size

    inner class OrderSubtotalViewHolder(private val binding: ItemSubTotalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.txvProductName.text = order.productName
            binding.txvProductSubTotal.text = (order.price * order.quantity).toRupiah()
        }
    }
}