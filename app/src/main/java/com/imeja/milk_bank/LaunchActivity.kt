package com.imeja.milk_bank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(
            Intent(
                this@LaunchActivity,
                DashboardActivity::class.java
            )
        )
        finish()
    }
}