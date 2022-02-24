package com.ibnu.foodcourt.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibnu.foodcourt.R
import com.ibnu.foodcourt.data.model.Product
import com.ibnu.foodcourt.data.remote.network.ApiResponse
import com.ibnu.foodcourt.databinding.HomeFragmentBinding
import com.ibnu.foodcourt.ui.home.adapter.CategoryAdapter
import com.ibnu.foodcourt.ui.home.adapter.CategoryItemHandler
import com.ibnu.foodcourt.ui.home.adapter.ProductAdapter
import com.ibnu.foodcourt.ui.home.adapter.ProductItemHandler
import com.ibnu.foodcourt.utils.SharedPreferenceManager
import com.ibnu.foodcourt.utils.ext.popTap
import com.ibnu.foodcourt.utils.ext.showExitFoodCourtDialog
import com.ibnu.foodcourt.utils.ext.toRupiah
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
@SuppressLint("SetTextI18n")
class HomeFragment : Fragment(), ProductItemHandler, CategoryItemHandler {

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoriesAdapter: CategoryAdapter
    private var isAlreadyLoadingShimmering = false
    private var cartTotalPrice = 0
    private var cartQuantity = 0

    private lateinit var pref: SharedPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().showExitFoodCourtDialog()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = SharedPreferenceManager(requireContext())

        if(cartQuantity > 0){
            binding.txvTotalPrice.text = cartTotalPrice.toRupiah()
            binding.txvCartQuantity.text = "$cartQuantity items"
        } else {
            loadLocalCartData()
        }

        initiateAdapters()
        showCategories()
        showProducts(2)

        binding.btnPerson.setOnClickListener {
            it.popTap()
            isAlreadyLoadingShimmering = false
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment2)
            }, 200)
        }

        binding.btnCart.setOnClickListener {
            it.popTap()
            isAlreadyLoadingShimmering = false
            Handler(Looper.getMainLooper()).postDelayed({

                findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
            }, 200)
        }
    }

    private fun showProducts(categoryId: Int) {
        val token = pref.getToken ?: ""
        val standId = pref.getStandId
        viewModel.getProducts(standId, categoryId, token).observe(viewLifecycleOwner, { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    Timber.d("Loading")
                    showLoading(true)
                }
                is ApiResponse.Error -> {
                    showLoading(false)
                    Timber.d("Error ${response.errorMessage}")
                }
                is ApiResponse.Success -> {
                    showLoading(false)
                    binding.txvEmptyText.visibility = View.GONE
                    binding.rvMenu.visibility = View.VISIBLE
                    productAdapter.setData(response.data)
                }
                is ApiResponse.Empty -> {
                    binding.txvEmptyText.visibility = View.VISIBLE
                    binding.rvMenu.visibility = View.INVISIBLE
                    showLoading(false)
                }
                else -> {
                    showLoading(false)
                    Timber.d("Unknown Error")
                }
            }
        })
    }

    private fun showCategories() {
        val token = pref.getToken ?: ""
        viewModel.getCategories(token).observe(viewLifecycleOwner, { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    Timber.d("Loading")
                }
                is ApiResponse.Error -> {
                    Timber.d("Error ${response.errorMessage}")
                }
                is ApiResponse.Success -> {
                    categoriesAdapter.setData(response.data)
                }
                else -> {
                    Timber.d("Unknown Error")
                }
            }
        })
    }

    private fun initiateAdapters() {
        productAdapter = ProductAdapter(this)
        binding.rvMenu.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = productAdapter
        }

        categoriesAdapter = CategoryAdapter(this)
        binding.rvCategories.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (!isAlreadyLoadingShimmering) {
            if (isLoading) {
                binding.shimmeringLoadingHome.startShimmer()
                binding.shimmeringLoadingHome.showShimmer(true)
                binding.loadingLayout.visibility = View.VISIBLE
            } else {
                binding.shimmeringLoadingHome.stopShimmer()
                binding.shimmeringLoadingHome.showShimmer(false)
                binding.loadingLayout.visibility = View.GONE
                isAlreadyLoadingShimmering = true
            }
        } else {
            if (isLoading) {
                binding.loadingCircular.visibility = View.VISIBLE
            } else {
                binding.loadingCircular.visibility = View.GONE
            }
        }
    }

    private fun loadLocalCartData(){
        viewModel.getOrderItemTotal().observe(viewLifecycleOwner, { totalItem ->
            cartQuantity = totalItem
            binding.txvCartQuantity.text = "$cartQuantity items"
        })

        viewModel.getOrderTotalPrice().observe(viewLifecycleOwner, { totalPrice ->
            cartTotalPrice = totalPrice
            binding.txvTotalPrice.text = cartTotalPrice.toRupiah()
        })
    }

    override fun addToCart(product: Product, price: Int) {
        cartQuantity++
        cartTotalPrice += price

        binding.txvTotalPrice.text = cartTotalPrice.toRupiah()
        binding.txvCartQuantity.text = "$cartQuantity items"

        viewModel.insertProductToCart(product)
    }

    override fun onCategoryItemClicked(categoryId: Int) {
        showProducts(categoryId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}