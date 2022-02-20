package com.ibnu.foodcourt.ui.profile

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ibnu.foodcourt.R
import com.ibnu.foodcourt.data.remote.network.ApiResponse
import com.ibnu.foodcourt.databinding.ProfileFragmentBinding
import com.ibnu.foodcourt.utils.ConstVal.KEY_STAND_ID
import com.ibnu.foodcourt.utils.ConstVal.KEY_TOKEN
import com.ibnu.foodcourt.utils.ConstVal.KEY_USER_ID
import com.ibnu.foodcourt.utils.SharedPreferenceManager
import com.ibnu.foodcourt.utils.UiConstValue
import com.ibnu.foodcourt.utils.ext.popTap
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: SharedPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = SharedPreferenceManager(requireContext())
        val token = pref.getToken ?: ""

        initiateAppBar()
        getProfileData(token)

        binding.btnLogout.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                logoutDialog()
            }, UiConstValue.FAST_ANIMATION_TIME)
        }

        binding.btnReportTransaction.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({

            }, UiConstValue.FAST_ANIMATION_TIME)
        }

        binding.btnAddProduct.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
              findNavController().navigate(R.id.action_profileFragment_to_addProductFragment)
            }, UiConstValue.FAST_ANIMATION_TIME)
        }
    }

    private fun getProfileData(token: String) {
        viewModel.getUserProfile(token).observe(viewLifecycleOwner, { response ->
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
                    val user = response.data
                    binding.txvProfileName.text = user.name
                }
                else -> {
                    showLoading(false)
                    Timber.d("Unknown Error")
                }
            }

        })
    }

    private fun initiateAppBar() {
        binding.appBar.tvToolbarTitle.text = "Profile"
        binding.appBar.imgBack.setOnClickListener {
            it.popTap()
            findNavController().popBackStack()
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmeringLoadingProfile.startShimmer()
            binding.shimmeringLoadingProfile.showShimmer(true)
            binding.loadingLayout.visibility = View.VISIBLE
        } else {
            binding.shimmeringLoadingProfile.stopShimmer()
            binding.shimmeringLoadingProfile.showShimmer(false)
            binding.loadingLayout.visibility = View.GONE
        }
    }

    private fun logoutDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Logout")
            setMessage("Apakah Anda yakin untuk logout?")
            setNegativeButton("Tidak") { p0, _ ->
                p0.dismiss()
            }
            setPositiveButton("IYA") { _, _ ->
                try {
                    pref.clearPreferenceByKey(KEY_TOKEN)
                    pref.clearPreferenceByKey(KEY_STAND_ID)
                    pref.clearPreferenceByKey(KEY_USER_ID)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                }
            }
        }.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}