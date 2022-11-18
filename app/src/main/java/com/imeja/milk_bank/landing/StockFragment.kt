package com.imeja.milk_bank.landing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imeja.milk_bank.R
import com.imeja.milk_bank.databinding.FragmentStockBinding
import com.imeja.milk_bank.utilities.showLoadingProgress

class StockFragment : Fragment() {
    private lateinit var binding: FragmentStockBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStockBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingProgress(binding.loading)
    }

}