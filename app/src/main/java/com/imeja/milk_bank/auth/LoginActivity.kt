package com.imeja.milk_bank.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.imeja.milk_bank.CoreApp
import com.imeja.milk_bank.DashboardActivity
import com.imeja.milk_bank.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            CoreApp.setSignedIn(this@LoginActivity,true)
            btnSignin.setOnClickListener {
                startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
            }
        }
    }

}