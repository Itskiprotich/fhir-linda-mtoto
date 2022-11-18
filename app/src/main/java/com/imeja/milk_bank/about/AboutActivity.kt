package com.imeja.milk_bank.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.imeja.milk_bank.R
import com.imeja.milk_bank.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.apply {
            setSupportActionBar(this)
            title = "About Us"
            // center the title
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            //set title color to white
            setTitleTextColor(resources.getColor(R.color.white))
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24)   // set back arrow in toolbar
            setNavigationOnClickListener {
                finish() // close this activity and return to preview activity (if there is any)
            }
        }

    }
}