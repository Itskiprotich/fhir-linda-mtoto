package com.imeja.milk_bank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.imeja.milk_bank.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}