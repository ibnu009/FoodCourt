package com.ibnu.foodcourt.ui.onboarding

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.ibnu.foodcourt.R
import com.ibnu.foodcourt.databinding.FragmentOnBoardingFirstBinding
import com.ibnu.foodcourt.utils.ConstVal.KEY_IS_ALREADY_INTRODUCED
import com.ibnu.foodcourt.utils.SharedPreferenceManager
import com.ibnu.foodcourt.utils.UiConstValue
import com.ibnu.foodcourt.utils.ext.popTap

class OnBoardingFirstFragment : Fragment() {

    private var _binding: FragmentOnBoardingFirstBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: SharedPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingFirstBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = SharedPreferenceManager(requireContext())

        val pager = activity?.findViewById<ViewPager2>(R.id.onBoardingViewPager)

        binding.btnNext.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                pager?.currentItem = 1
            }, UiConstValue.FAST_ANIMATION_TIME)
        }

        binding.circle2.setOnClickListener {
            pager?.currentItem = 1
        }

        binding.btnSkip.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                try {
                    pref.setBooleanPreference(KEY_IS_ALREADY_INTRODUCED, true)
                } finally {
                    findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
                }
            }, UiConstValue.FAST_ANIMATION_TIME)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}