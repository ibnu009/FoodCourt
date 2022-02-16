package com.ibnu.foodcourt.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnBoardingViewPagerAdapter(
    fa: FragmentActivity,
) :
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                OnBoardingFirstFragment()
            }
            1 -> {
                OnBoardingSecondFragment()
            }
            else -> OnBoardingFirstFragment()
        }
    }
}