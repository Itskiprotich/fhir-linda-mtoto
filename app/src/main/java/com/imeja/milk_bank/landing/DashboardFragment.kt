package com.imeja.milk_bank.landing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imeja.milk_bank.R
import com.imeja.milk_bank.adapters.AdapterDashboard
import com.imeja.milk_bank.databinding.ActivityAboutBinding
import com.imeja.milk_bank.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            includeViewpagerTabsLayout.viewpager.adapter =
                AdapterDashboard(childFragmentManager, requireContext())
            includeViewpagerTabsLayout.tabLayout.setupWithViewPager(includeViewpagerTabsLayout.viewpager)
            includeViewpagerTabsLayout.viewpagerTabsLayout.visibility = View.VISIBLE
        }
    }
}