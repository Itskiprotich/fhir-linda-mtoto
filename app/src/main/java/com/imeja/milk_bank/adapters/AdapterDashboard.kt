package com.imeja.milk_bank.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.imeja.milk_bank.R
import com.imeja.milk_bank.landing.inner.ActiveFragment
import com.imeja.milk_bank.landing.inner.DischargedFragment

class AdapterDashboard(fragmentManager: FragmentManager, val context: Context) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ActiveFragment()
            else -> DischargedFragment()
        }
    }

    override fun getCount() = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.getString(R.string.menu_active)
            else -> context.getString(R.string.menu_discharged)
        }
    }

}