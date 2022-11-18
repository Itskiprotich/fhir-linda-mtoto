package com.imeja.milk_bank.landing.inner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imeja.milk_bank.databinding.FragmentActiveBinding
import com.imeja.milk_bank.utilities.showLoadingProgress

class ActiveFragment : Fragment() {
    private lateinit var binding: FragmentActiveBinding

    //     oncreate function
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingProgress(binding.loading)
    }
}