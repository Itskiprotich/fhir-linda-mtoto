package com.imeja.milk_bank.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.fhir.datacapture.QuestionnaireFragment
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.imeja.milk_bank.R
import com.imeja.milk_bank.activities.inner.RegisterFragment
import com.imeja.milk_bank.databinding.ActivityRegistrationBinding
import com.imeja.milk_bank.landing.DashboardFragment
import com.imeja.milk_bank.viewmodels.AddPatientViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private val viewModel: AddPatientViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.apply {
            setSupportActionBar(this)
            title = getString(R.string.app_registration)
            // center the title
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            //set title color to white
            setTitleTextColor(resources.getColor(R.color.black))
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24) // set back arrow in toolbar
            setNavigationOnClickListener {
              exitPage()// close this activity and return to preview activity (if there is any)
            }
        }
        replaceFragment(
            supportFragmentManager,
            RegisterFragment::class.java.simpleName,
            RegisterFragment()
        )

        binding.apply {

        }

    }

    fun exitPage(){
        this@RegistrationActivity.finish()
    }

    private fun replaceFragment(
        fragmentManager: FragmentManager,
        simpleName: String,
        dashboardFragment: Fragment
    ) {
        val bundle =
            bundleOf(RegisterFragment.QUESTIONNAIRE_FILE_PATH_KEY to "registration.json")
        dashboardFragment.arguments = bundle
        fragmentManager.beginTransaction().add(R.id.contentContainer, dashboardFragment, simpleName)
            .commit()
    }

}