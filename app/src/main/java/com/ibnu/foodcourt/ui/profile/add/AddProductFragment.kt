package com.ibnu.foodcourt.ui.profile.add

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibnu.foodcourt.R
import com.ibnu.foodcourt.data.remote.network.ApiResponse
import com.ibnu.foodcourt.databinding.AddProductFragmentBinding
import com.ibnu.foodcourt.ui.home.adapter.CategoryAdapter
import com.ibnu.foodcourt.ui.home.adapter.CategoryItemHandler
import com.ibnu.foodcourt.ui.home.adapter.ProductAdapter
import com.ibnu.foodcourt.utils.NumberTextWatcher
import com.ibnu.foodcourt.utils.PostStateHandler
import com.ibnu.foodcourt.utils.SharedPreferenceManager
import com.ibnu.foodcourt.utils.UiConstValue
import com.ibnu.foodcourt.utils.ext.getFilePathFromUri
import com.ibnu.foodcourt.utils.ext.getImageUri
import com.ibnu.foodcourt.utils.ext.popTap
import com.ibnu.foodcourt.utils.ext.showOKDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AddProductFragment : Fragment(), PostStateHandler, CategoryItemHandler {

    private val viewModel: AddProductViewModel by viewModels()

    private var _binding: AddProductFragmentBinding? = null
    private val binding get() = _binding!!
    private var imagePath: String? = null
    private lateinit var pref: SharedPreferenceManager
    private lateinit var categoriesAdapter: CategoryAdapter
    private var categoryId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exitAddProduct()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddProductFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = SharedPreferenceManager(requireContext())
        val standId: Int = pref.getStandId
        val token: String = pref.getToken ?: ""
        viewModel.postState = this
        initiateAppBar()
        initiateAdapters()
        showCategories()

        binding.btnProductImage.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                showImageMenu()
            }, UiConstValue.FAST_ANIMATION_TIME)
        }

        binding.edtPrice.addTextChangedListener(NumberTextWatcher(binding.edtPrice))
        binding.edtPrice.keyListener = DigitsKeyListener.getInstance("0123456789")
        binding.btnSave.setOnClickListener {
            it.popTap()

            Handler(Looper.getMainLooper()).postDelayed({
                viewModel.validateUploadProduct(
                    requireContext(),
                    viewLifecycleOwner,
                    standId.toString(),
                    categoryId.toString(),
                    binding.edtName.text.toString(),
                    binding.edtPrice.text.toString(),
                    imagePath ?: "",
                    token
                )
            }, UiConstValue.FAST_ANIMATION_TIME)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initiateAppBar() {
        binding.appBar.tvToolbarTitle.text = "New Product"
        binding.appBar.imgBack.setOnClickListener {
            it.popTap()
            findNavController().popBackStack()
        }
    }

    private fun initiateAdapters() {
        categoriesAdapter = CategoryAdapter(this)
        binding.rvCategories.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = categoriesAdapter
        }
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.bgDim.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnSave.isClickable = !isLoading
    }

    private fun showImageMenu() {
        AlertDialog.Builder(requireActivity())
            .setTitle("Pilih metode pengambilan Gambar")
            .setItems(R.array.pictures) { _, p1 ->
                if (p1 == 0) {
                    takePictureRegistration.launch()
                } else {
                    pickFileImage.launch("image/*")
                }
            }.create().show()
    }

    private val takePictureRegistration =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                val uri = requireActivity().getImageUri(bitmap)
                imagePath = requireActivity().getFilePathFromUri(uri)
                binding.btnProductImage.setImageBitmap(bitmap)
            }
        }

    private val pickFileImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imagePath = requireActivity().getFilePathFromUri(uri)
                binding.btnProductImage.setImageURI(uri)
            }
        }

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Success!")
            setMessage(message)
            setPositiveButton("Ok") { p0, _ ->
                p0.dismiss()
                findNavController().popBackStack()
            }
        }.create().show()
    }

    private fun categoryNameToCategoryId(categoryName: String): String {
        var categoryId = "2"
        when {
            categoryName.equals("Minuman", true) -> {
                categoryId = "2"
            }
            categoryName.equals("Makanan", true) -> {
                categoryId = "3"
            }
            categoryName.equals("Snack", true) -> {
                categoryId = "4"
            }
            categoryName.equals("Roti", true) -> {
                categoryId = "5"
            }
        }
        return categoryId
    }

    fun exitAddProduct() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Batalkan Add Product")
            setMessage("Apakah anda yakin untuk membatalkan add product?")
            setNegativeButton("Tidak") { p0, _ ->
                p0.dismiss()
            }
            setPositiveButton("IYA") { _, _ ->
                findNavController().popBackStack()
            }
        }.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onInitiating() {
        showLoading(true)
    }

    override fun onSuccess(message: String) {
        showLoading(false)
        showSuccessDialog(message)
    }

    override fun onFailure(message: String) {
        showLoading(false)
        requireContext().showOKDialog("Gagal", message)
    }

    override fun onCategoryItemClicked(categoryId: Int) {
        this.categoryId = categoryId
    }

}