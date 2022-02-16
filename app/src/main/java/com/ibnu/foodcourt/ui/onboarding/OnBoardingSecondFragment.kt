package com.ibnu.foodcourt.ui.onboarding

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.ibnu.foodcourt.R
import com.ibnu.foodcourt.databinding.FragmentOnBoarding2Binding
import com.ibnu.foodcourt.utils.ConstVal
import com.ibnu.foodcourt.utils.SharedPreferenceManager
import com.ibnu.foodcourt.utils.UiConstValue
import com.ibnu.foodcourt.utils.ext.popTap

class OnBoardingSecondFragment : Fragment() {
    private var _binding: FragmentOnBoarding2Binding? = null
    private val binding get() = _binding!!
    private lateinit var pref: SharedPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoarding2Binding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = SharedPreferenceManager(requireContext())

        val pager = activity?.findViewById<ViewPager2>(R.id.onBoardingViewPager)

        binding.btnPrev.setOnClickListener {
            pager?.currentItem = 0
        }

        binding.circle1.setOnClickListener {
            pager?.currentItem = 0
        }

        binding.btnSkip.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                navigateToLogin()
            }, UiConstValue.FAST_ANIMATION_TIME)
        }

        binding.btnStart.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                navigateToLogin()
            }, UiConstValue.FAST_ANIMATION_TIME)
        }
    }

    private fun navigateToLogin(){
        try {
            pref.setBooleanPreference(ConstVal.KEY_IS_ALREADY_INTRODUCED, true)
        } finally {
            findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}