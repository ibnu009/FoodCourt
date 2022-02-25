package com.ibnu.foodcourt.ui.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibnu.foodcourt.R
import com.ibnu.foodcourt.data.model.Order
import com.ibnu.foodcourt.databinding.CartFragmentBinding
import com.ibnu.foodcourt.ui.cart.adapter.OrderAdapter
import com.ibnu.foodcourt.ui.cart.adapter.OrderItemHandler
import com.ibnu.foodcourt.ui.cart.adapter.OrderSubtotalAdapter
import com.ibnu.foodcourt.utils.UiConstValue
import com.ibnu.foodcourt.utils.ext.popTap
import com.ibnu.foodcourt.utils.ext.toRupiah
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
@SuppressLint("SetTextI18n")
class CartFragment : Fragment(), OrderItemHandler {

    private val viewModel: CartViewModel by viewModels()

    private var _binding: CartFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderAdapter: OrderAdapter
    private lateinit var orderSubtotalAdapter: OrderSubtotalAdapter

    private var grandTotal = 0
    private var totalItems = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CartFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateAdapters()
        initiateAppBar()
        setGrandTotal()
        viewModel.getAllOrders().observe(viewLifecycleOwner, {
            orderAdapter.setData(it)
            orderSubtotalAdapter.setData(it)
        })

        binding.btnCheckOut.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
            }, UiConstValue.FAST_ANIMATION_TIME)
        }
    }

    private fun initiateAppBar() {
        binding.appBar.btnCancelOrder.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().popBackStack()
            }, UiConstValue.FAST_ANIMATION_TIME)
        }

        binding.appBar.btnCancelOrder.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                showDeleteAllItemDialog()
            }, UiConstValue.FAST_ANIMATION_TIME)
        }
    }

    private fun initiateAdapters() {
        orderAdapter = OrderAdapter(this)
        binding.rvOrder.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = orderAdapter
            isNestedScrollingEnabled = false
        }

        orderSubtotalAdapter = OrderSubtotalAdapter()
        binding.rvOrderSubTotal.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = orderSubtotalAdapter
            isNestedScrollingEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onIncrementOrderQuantity(order: Order, position: Int) {
        viewModel.updateOrder(order)
        Timber.d("Position is $position")
        incrementGrandTotal(order)
        orderSubtotalAdapter.changeData(order, position)
    }

    override fun onDecrementOrderQuantity(order: Order, position: Int) {
        viewModel.updateOrder(order)
        Timber.d("Position is $position")
        if(order.quantity > 1){
            decrementGrandTotal(order)
        }
        orderSubtotalAdapter.changeData(order, position)
    }

    override fun onDeleteOrder(order: Order, position: Int) {
        showDeleteItemDialog(order, position)
    }

    private fun setGrandTotal() {
        viewModel.getOrderItemTotal().observe(viewLifecycleOwner, {
            binding.txvProductTotal.text = "$it Items"
            totalItems = it
        })

        viewModel.getOrderTotalPrice().observe(viewLifecycleOwner, {
            binding.txvGrandTotalSmall.text = it.toRupiah()
            binding.txvGrandTotal.text = it.toRupiah()
            grandTotal = it
        })
    }

    private fun decrementGrandTotal(order: Order) {
        grandTotal -= order.price
        totalItems--

        binding.txvProductTotal.text = "$totalItems Items"
        binding.txvGrandTotalSmall.text = grandTotal.toRupiah()
        binding.txvGrandTotal.text = grandTotal.toRupiah()
    }

    private fun incrementGrandTotal(order: Order) {
        grandTotal += order.price
        totalItems++

        Timber.d("incrementedGrandTotal is $grandTotal")

        binding.txvProductTotal.text = "$totalItems Items"
        binding.txvGrandTotalSmall.text = grandTotal.toRupiah()
        binding.txvGrandTotal.text = grandTotal.toRupiah()
    }

    private fun deleteGrandTotal(order: Order){
        grandTotal -= (order.price * order.quantity)
        totalItems -= order.quantity

        binding.txvProductTotal.text = "$totalItems Items"
        binding.txvGrandTotalSmall.text = grandTotal.toRupiah()
        binding.txvGrandTotal.text = grandTotal.toRupiah()
    }

    private fun showDeleteItemDialog(order: Order, position: Int) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Hapus Pesanan")
            setMessage("Apakah Anda yakin untuk menghapus ${order.productName} dari keranjang?")
            setNegativeButton("Tidak") { p0, _ ->
                p0.dismiss()
            }
            setPositiveButton("IYA") { p0, _ ->
                p0.dismiss()
                viewModel.removeOrder(order)
                orderAdapter.removeItem(position)
                orderSubtotalAdapter.removeItem(position)
                deleteGrandTotal(order)
            }
        }.create().show()
    }

    private fun showDeleteAllItemDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Batalkan Pesanan?")
            setMessage("Apakah Anda yakin untuk menghapus semua pesanan dari keranjang?")
            setNegativeButton("Tidak") { p0, _ ->
                p0.dismiss()
            }
            setPositiveButton("IYA") { _, _ ->
                viewModel.clearOrder()
                findNavController().popBackStack()
            }
        }.create().show()
    }
}