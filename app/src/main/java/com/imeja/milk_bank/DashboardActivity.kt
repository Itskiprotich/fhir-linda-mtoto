package com.imeja.milk_bank

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.google.android.fhir.sync.State
import com.imeja.milk_bank.about.AboutActivity
import com.imeja.milk_bank.databinding.ActivityDashboardBinding
import com.imeja.milk_bank.landing.*
import com.imeja.milk_bank.ui.patients.RegistrationFragment
import com.imeja.milk_bank.viewmodels.MainViewModel
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.apply {
            setSupportActionBar(this)
            title = ""
        }

        handleBottomBarTabClicks()
        binding.apply {
            bottomBar.selectedItemId = R.id.menu_home
        }
        handleMainAction()
    }

    private fun handleMainAction() {
        observeLastSyncTime()
        observeSyncState()
        viewModel.updateLastSyncTimestamp()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun observeSyncState() {
        lifecycleScope.launch {
            viewModel.pollState.collect {

                when (it) {
                    is State.Started -> showToast("Sync: started")
                    is State.InProgress -> showToast(
                        "Sync: in progress with ${it.resourceType?.name}"
                    )
                    is State.Finished -> {
                        showToast("Sync: succeeded at ${it.result.timestamp}")
                        viewModel.updateLastSyncTimestamp()
                    }
                    is State.Failed -> {
                        showToast("Sync: failed at ${it.result.timestamp}")
                        viewModel.updateLastSyncTimestamp()
                    }
                    else -> showToast("Sync: unknown state.")
                }
            }
        }
    }

    private fun observeLastSyncTime() {
        viewModel.lastSyncTimestampLiveData.observe(
            this
        ) {
//            CoreApp.updateSyncTime(this@MainActivity, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_about_us) {
            startActivity(Intent(this@DashboardActivity, AboutActivity::class.java))
        }
        if (id == R.id.action_share) {
//            SharingUtil.shareApp(this)
        }
        if (id == R.id.action_rate) {
//            SharingUtil.openAppInPlaystore(this)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleBottomBarTabClicks() {
        binding.bottomBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> displaySelectedFragment(DashboardFragment::class.java.simpleName)
                R.id.menu_hmb -> displaySelectedFragment(MilkbankFragment::class.java.simpleName)
                R.id.menu_stock -> displaySelectedFragment(StockFragment::class.java.simpleName)
                R.id.menu_statistics -> displaySelectedFragment(StatisticsFragment::class.java.simpleName)
                R.id.menu_profile -> displaySelectedFragment(ProfileFragment::class.java.simpleName)
                else -> displaySelectedFragment(DashboardFragment::class.java.simpleName)
            }

            true
        }
    }

    private fun replaceFragment(
        fragmentManager: FragmentManager,
        currentTag: String,
        clazz: Fragment
    ) {
        val selectedFragment = fragmentManager.findFragmentByTag(currentTag)
        val allTags = arrayListOf(
            DashboardFragment::class.java.simpleName,
            MilkbankFragment::class.java.simpleName,
            StockFragment::class.java.simpleName,
            StatisticsFragment::class.java.simpleName,
            ProfileFragment::class.java.simpleName
        )
        if (selectedFragment != null) {
            fragmentManager.beginTransaction().show(selectedFragment).commit()
        } else {
            fragmentManager.beginTransaction().add(R.id.contentContainer, clazz, currentTag)
                .commit()
        }
        allTags.remove(currentTag)
        for (fragmentTag in allTags) {
            val fragmentToHide = fragmentManager.findFragmentByTag(fragmentTag)
            if (fragmentToHide != null) {
                fragmentManager.beginTransaction().hide(fragmentToHide).commit()
            }
        }
    }

    private fun displaySelectedFragment(fragmentTag: String) {
        val fm = supportFragmentManager
        when (fragmentTag) {
            DashboardFragment::class.java.simpleName -> {
                updateTitle(getString(R.string.menu_home))
                replaceFragment(fm, DashboardFragment::class.java.simpleName, DashboardFragment())
            }
            MilkbankFragment::class.java.simpleName -> {
                updateTitle(getString(R.string.menu_hmb))
                replaceFragment(fm, MilkbankFragment::class.java.simpleName, MilkbankFragment())
            }
            StockFragment::class.java.simpleName -> {
                updateTitle(getString(R.string.menu_stock))
                replaceFragment(fm, StockFragment::class.java.simpleName, StockFragment())
            }
            StatisticsFragment::class.java.simpleName -> {
                updateTitle(getString(R.string.menu_statistics))
                replaceFragment(fm, StatisticsFragment::class.java.simpleName, StatisticsFragment())
            }
            ProfileFragment::class.java.simpleName -> {
                updateTitle(getString(R.string.menu_profile))
                replaceFragment(fm, ProfileFragment::class.java.simpleName, ProfileFragment())
            }
        }
    }

    private fun updateTitle(string: String) {
        binding.apply {
            toolbarTitle.text = string
        }
    }

    fun navigateTo(host: Fragment,title:String) {
//        val bundle =
//            bundleOf(RegistrationFragment.QUESTIONNAIRE_FILE_PATH_KEY to "registration.json")
//        findNavController(R.id.contentContainer).navigate(
//            R.id.registrationFragment,
//            bundle
//        )
        val bundle = Bundle()
        bundle.putString(RegistrationFragment.QUESTIONNAIRE_FILE_PATH_KEY, "registration.json")
        host.arguments = bundle
        updateTitle(title)
        replaceFragment(supportFragmentManager, host::class.java.simpleName, host)
    }
}