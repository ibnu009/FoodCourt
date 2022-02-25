package com.ibnu.foodcourt.ui.checkout

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ibnu.foodcourt.R
import com.ibnu.foodcourt.data.model.Order
import com.ibnu.foodcourt.data.remote.network.ApiResponse
import com.ibnu.foodcourt.data.remote.request.Transaction
import com.ibnu.foodcourt.data.remote.request.TransactionBody
import com.ibnu.foodcourt.databinding.CheckoutFragmentBinding
import com.ibnu.foodcourt.utils.SharedPreferenceManager
import com.ibnu.foodcourt.utils.UiConstValue
import com.ibnu.foodcourt.utils.ext.popTap
import com.ibnu.foodcourt.utils.ext.showOKDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CheckoutFragment : Fragment() {

    private val viewModel: CheckoutViewModel by viewModels()

    private var _binding: CheckoutFragmentBinding? = null
    private val binding get() = _binding!!

    private var isDineIn = true

    private val listOrder = ArrayList<Order>()
    private lateinit var pref: SharedPreferenceManager
    var token = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CheckoutFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = SharedPreferenceManager(requireContext())
        token = pref.getToken ?: ""
        viewModel.getAllOrders().observe(viewLifecycleOwner, {
            listOrder.addAll(it)
        })

        binding.btnPay.setOnClickListener {
            it.popTap()
            val buyerName = binding.edtBuyerName.text.toString()
            Handler(Looper.getMainLooper()).postDelayed({
                when {
                    buyerName.isBlank() -> binding.edtBuyerName.error = "Masukkan nama pembeli"
                    else -> {
                        makeTransaction()
                    }
                }
            }, UiConstValue.FAST_ANIMATION_TIME)
        }

        binding.lyDineIn.setOnClickListener {
            isDineIn = true
            binding.edtTable.isEnabled = isDineIn
            binding.edtTable.isClickable = isDineIn

            binding.cardDineIn.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.bg_order_ligher
                )
            )
            binding.txvDineIn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

            binding.cardTakeAway.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            binding.txvTakeAway.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey_800
                )
            )

            binding.indicatorDineIn.visibility = View.VISIBLE
            binding.indicatorTakeAway.visibility = View.GONE
        }

        binding.lyTakeAway.setOnClickListener {
            isDineIn = false
            binding.edtTable.isEnabled = isDineIn
            binding.edtTable.isClickable = isDineIn

            binding.cardTakeAway.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.bg_order_ligher
                )
            )
            binding.txvTakeAway.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            binding.cardDineIn.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            binding.txvDineIn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey_800
                )
            )

            binding.indicatorDineIn.visibility = View.GONE
            binding.indicatorTakeAway.visibility = View.VISIBLE
        }
    }

    private fun makeTransaction() {
        val listTransaction = ArrayList<Transaction>()
        listOrder.map { order ->
            listTransaction.add(
                Transaction(
                    standId = order.standId,
                    productId = order.productId,
                    table = binding.edtTable.text.toString().toInt(),
                    quantity = order.quantity,
                    status = 1,
                    buyerName = binding.edtBuyerName.text.toString()
                )
            )
        }
        val request = TransactionBody(data = listTransaction)

        viewModel.postTransaction(request, token = token).observe(viewLifecycleOwner, { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    Timber.d("Loading")
                    showLoading(true)
                }
                is ApiResponse.Error -> {
                    Timber.d("Error ${response.errorMessage}")
                    showLoading(false)
                    requireContext().showOKDialog(UiConstValue.ERROR_TITLE, response.errorMessage)
                }
                is ApiResponse.Success -> {
                    showLoading(false)
                    try {
                        viewModel.clearOrder()
                    } finally {
                        findNavController().navigate(R.id.action_checkoutFragment_to_postCheckoutFragment)
                    }
                }
                else -> {
                    Timber.d("Unknown Error")
                }
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.bgDim.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.edtTable.isClickable = !isLoading
        binding.edtBuyerName.isEnabled = !isLoading
        binding.lyTakeAway.isClickable = !isLoading
        binding.lyDineIn.isClickable = !isLoading
        binding.btnPay.isClickable = !isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}