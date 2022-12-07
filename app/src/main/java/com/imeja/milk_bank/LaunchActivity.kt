package com.imeja.milk_bank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.imeja.milk_bank.auth.LoginActivity

class LaunchActivity : AppCompatActivity() {
    private var isLoggedIn = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isLoggedIn = CoreApp.isSignedIn(this@LaunchActivity)
        if (isLoggedIn) {
            startActivity(Intent(this@LaunchActivity, DashboardActivity::class.java))
        } else {
            startActivity(
                Intent(
                    this@LaunchActivity,
                    LoginActivity::class.java
                )
            )
        }
        finish()
    }
}