package com.ibnu.foodcourt.ui.checkout

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ibnu.foodcourt.R
import com.ibnu.foodcourt.databinding.FragmentPostCheckoutBinding
import com.ibnu.foodcourt.utils.UiConstValue
import com.ibnu.foodcourt.utils.ext.popTap
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostCheckoutFragment : Fragment() {

    private var _binding: FragmentPostCheckoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_postCheckoutFragment_to_homeFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostCheckoutBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_postCheckoutFragment_to_homeFragment)
            }, UiConstValue.FAST_ANIMATION_TIME)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}