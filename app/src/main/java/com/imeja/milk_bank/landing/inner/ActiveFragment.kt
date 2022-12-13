package com.imeja.milk_bank.landing.inner

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.sync.State
import com.imeja.milk_bank.CoreApp
import com.imeja.milk_bank.DashboardActivity
import com.imeja.milk_bank.R
import com.imeja.milk_bank.activities.RegistrationActivity
import com.imeja.milk_bank.databinding.FragmentActiveBinding
import com.imeja.milk_bank.viewmodels.MainViewModel
import com.imeja.milk_bank.viewmodels.PatientListViewModel
import kotlinx.coroutines.launch
import com.imeja.milk_bank.adapters.PatientItemRecyclerViewAdapter
import timber.log.Timber
import com.imeja.milk_bank.models.PatientItem
import com.imeja.milk_bank.ui.patients.RegistrationFragment
import com.imeja.milk_bank.utilities.hideLoadingProgress
import com.imeja.milk_bank.utilities.showLoadingProgress

class ActiveFragment : Fragment() {
    private lateinit var fhirEngine: FhirEngine
    private lateinit var patientListViewModel: PatientListViewModel
    private lateinit var binding: FragmentActiveBinding
    private var patientList = ArrayList<PatientItem>()

    //    private lateinit var searchView: SearchView
    lateinit var adapterList: PatientItemRecyclerViewAdapter
    private lateinit var topBanner: LinearLayout
    private lateinit var syncStatus: TextView
    private val mainActivityViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fhirEngine = CoreApp.fhirEngine(requireContext())
        patientListViewModel =
            ViewModelProvider(
                this,
                PatientListViewModel.PatientListViewModelFactory(
                    requireActivity().application,
                    fhirEngine
                )
            ).get(PatientListViewModel::class.java)


        binding.includeBaseRecyclerviewLayout.baseRecyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
                setDrawable(ColorDrawable(Color.GRAY))
            }
        )
        patientListViewModel.liveSearchedPatients.observe(viewLifecycleOwner) {
            patientList.clear()
            patientList.addAll(it)
            adapterList = PatientItemRecyclerViewAdapter(patientList, this::onPatientItemClicked)
            binding.includeBaseRecyclerviewLayout.baseRecyclerView.adapter = adapterList
            binding.includeBaseRecyclerviewLayout.baseRecyclerView.layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapterList.submitList(patientList)
            adapterList.notifyDataSetChanged()

        }
        requireActivity()
            .onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        isEnabled = false
                        activity?.onBackPressed()
                    }
                }
            )

        binding.apply {
            btnAdd.setOnClickListener { onAddPatientClick() }
//            addPatient.setColorFilter(Color.WHITE)
        }

        lifecycleScope.launch {
            mainActivityViewModel.pollState.collect {
                Timber.d("onViewCreated: pollState Got status $it")
                when (it) {
                    is State.Started -> {
                        Timber.i("Sync: ${it::class.java.simpleName}")
                        fadeInTopBanner()
                    }
                    is State.InProgress -> {
                        Timber.i("Sync: ${it::class.java.simpleName} with ${it.resourceType?.name}")
                        fadeInTopBanner()
                    }
                    is State.Finished -> {
//                        Timber.i("Sync: ${it::class.java.simpleName} at ${it.timestamp}")
                        patientListViewModel.searchPatientsByName(
//                            searchView.query.toString().trim()
                            ""
                        )
                        mainActivityViewModel.updateLastSyncTimestamp()
                        fadeOutTopBanner(it)
                    }
                    is State.Failed -> {
//                        Timber.i("Sync: ${it::class.java.simpleName} at ${it.timestamp}")
                        patientListViewModel.searchPatientsByName(
//                            searchView.query.toString().trim()
                            ""
                        )
                        mainActivityViewModel.updateLastSyncTimestamp()
                        fadeOutTopBanner(it)
                    }
                    else -> {
                        Timber.i("Sync: Unknown state.")
                        patientListViewModel.searchPatientsByName(
//                            searchView.query.toString().trim()
                            ""
                        )
                        mainActivityViewModel.updateLastSyncTimestamp()
                        fadeOutTopBanner(it)
                    }
                }
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // hide the soft keyboard when the navigation drawer is shown on the screen.
//                searchView.clearFocus()
//                (requireActivity() as MainActivity).openNavigationDrawer()
                true
            }
            else -> false
        }
    }

    private fun onPatientItemClicked(patientItem: PatientItem) {
        patientListViewModel.dischargePatient(patientItem.resourceId)
        adapterList.notifyDataSetChanged()
//        findNavController()            .navigate(PatientListFragmentDirections.navigateToProductDetail(patientItem.resourceId))
    }

    private fun onAddPatientClick() {
//        val bundle =
//            bundleOf(RegistrationFragment.QUESTIONNAIRE_FILE_PATH_KEY to "client-registration.json")
//        findNavController(R.id.contentContainer).navigate(
//            R.id.customRegistrationFragment,
//            bundle
//        )
//        ((context as DashboardActivity)).navigateTo(RegistrationFragment(), "Registration")
//        findNavController()
//            .navigate(ActiveFragmentDirections.navigateToRegistration())
//        val navHostFragment = childFragmentManager.sup.findFragmentById(R.id.contentContainer) as NavHostFragment. // the id of your FragmentContainerView
//        navController = navHostFragment.navController

        startActivity(Intent(requireContext(), RegistrationActivity::class.java))
    }

    private fun fadeInTopBanner() {
        showLoadingProgress(binding.loading)
    }

    private fun fadeOutTopBanner(state: State) {
        hideLoadingProgress(binding.loading)
    }
}