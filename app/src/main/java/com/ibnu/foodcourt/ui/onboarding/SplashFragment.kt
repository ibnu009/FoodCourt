package com.ibnu.foodcourt.ui.onboarding

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ibnu.foodcourt.R
import com.ibnu.foodcourt.databinding.FragmentSplashBinding
import com.ibnu.foodcourt.utils.SharedPreferenceManager
import com.ibnu.foodcourt.utils.UiConstValue

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: SharedPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = SharedPreferenceManager(requireContext())
        val token = pref.getToken ?: ""
        val isAlreadyIntroduced = pref.isAlreadyIntroduced
        Handler(Looper.getMainLooper()).postDelayed({
            when {
                !isAlreadyIntroduced -> {
                    findNavController().navigate(R.id.action_splashFragment_to_onBoardingFragment)
                }
                token.isBlank() -> {
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }
                else -> {
                    findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                }
            }
        }, UiConstValue.LONG_ANIMATION_TIME)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}