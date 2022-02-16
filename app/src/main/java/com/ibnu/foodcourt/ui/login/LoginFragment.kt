package com.ibnu.foodcourt.ui.login

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
import com.ibnu.foodcourt.R
import com.ibnu.foodcourt.data.remote.network.ApiResponse
import com.ibnu.foodcourt.data.remote.request.LoginBody
import com.ibnu.foodcourt.databinding.LoginFragmentBinding
import com.ibnu.foodcourt.utils.ConstVal.KEY_TOKEN
import com.ibnu.foodcourt.utils.ConstVal.KEY_USER_ID
import com.ibnu.foodcourt.utils.SharedPreferenceManager
import com.ibnu.foodcourt.utils.UiConstValue
import com.ibnu.foodcourt.utils.UiConstValue.ERROR_TITLE
import com.ibnu.foodcourt.utils.ext.isEmailValid
import com.ibnu.foodcourt.utils.ext.popTap
import com.ibnu.foodcourt.utils.ext.showExitFoodCourtDialog
import com.ibnu.foodcourt.utils.ext.showOKDialog
import timber.log.Timber

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!
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
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = SharedPreferenceManager(requireContext())

        binding.btnLogin.setOnClickListener {
            it.popTap()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            Handler(Looper.getMainLooper()).postDelayed({
                when {
                    email.isBlank() -> binding.edtEmail.error = "Email tidak boleh kosong!"
                    !email.isEmailValid() -> binding.edtEmail.error = "Email tidak valid!"
                    password.isBlank() -> binding.edtPassword.error = "Password tidak boleh kosong!"
                    else -> {
                        val request = LoginBody(
                            email, password
                        )
                        loginUser(request)
                    }
                }
            }, UiConstValue.FAST_ANIMATION_TIME)
        }
    }

    private fun loginUser(request: LoginBody) {
        viewModel.login(request).observe(viewLifecycleOwner, { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    Timber.d("Loading")
                    showLoading(true)
                }
                is ApiResponse.Error -> {
                    Timber.d("Error ${response.errorMessage}")
                    showLoading(false)
                    requireContext().showOKDialog(ERROR_TITLE, response.errorMessage)
                }
                is ApiResponse.Success -> {
                    showLoading(false)
                    pref.setStringPreference(KEY_TOKEN, response.data.token)
                    pref.setIntPreference(KEY_USER_ID, response.data.user.id)

                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
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
        binding.edtEmail.isClickable = !isLoading
        binding.edtEmail.isEnabled = !isLoading
        binding.edtPassword.isClickable = !isLoading
        binding.edtPassword.isEnabled = !isLoading
        binding.btnLogin.isClickable = !isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}