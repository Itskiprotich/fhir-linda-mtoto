package com.imeja.milk_bank.ui.patients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.google.android.fhir.datacapture.QuestionnaireFragment
import com.imeja.milk_bank.R
import com.imeja.milk_bank.databinding.FragmentRegistrationBinding
import com.imeja.milk_bank.viewmodels.AddPatientViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*


class RegistrationFragment : Fragment() {
    private lateinit var patientId: String
    private var _binding: FragmentRegistrationBinding? = null
    private val viewModel: AddPatientViewModel by viewModels()
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = resources.getString(R.string.app_registration)
            setDisplayHomeAsUpEnabled(true)
        }
        updateArguments()
        onBackPressed()
        if (savedInstanceState == null) {
            addQuestionnaireFragment()
        }
        setHasOptionsMenu(true)

        binding.apply {

            btnSubmit.setOnClickListener {
                onSubmitAction()
            }
            btnCancel.setOnClickListener {
                showCancelScreenerQuestionnaireAlertDialog()
            }
        }

        patientId = generateUuid()

    }

    private fun onSubmitAction() {
        observeResourcesSaveAction()
        CoroutineScope(Dispatchers.IO).launch {
            val questionnaireFragment =
                childFragmentManager.findFragmentByTag(QUESTIONNAIRE_FRAGMENT_TAG) as QuestionnaireFragment

            patientId = generateUuid()
            viewModel.clientRegistration(
                questionnaireFragment.getQuestionnaireResponse(),
            )
        }
    }


    private fun generateUuid(): String {
        return UUID.randomUUID().toString()
    }

    private fun observeResourcesSaveAction() {
        viewModel.isPatientSaved.observe(viewLifecycleOwner) {

            if (it != null) {
                if (it) {
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Try again", Toast.LENGTH_SHORT).show()
                    return@observe
                }
            }

        }
    }

    private fun updateArguments() {
        requireArguments().putString(QUESTIONNAIRE_FILE_PATH_KEY, "registration.json")

    }

    private fun addQuestionnaireFragment() {
        try {
            val fragment = QuestionnaireFragment()
            fragment.arguments =
                bundleOf(QuestionnaireFragment.EXTRA_QUESTIONNAIRE_JSON_STRING to viewModel.questionnaire)
            childFragmentManager.commit {
                replace(
                    R.id.add_patient_container, fragment,
                    QUESTIONNAIRE_FRAGMENT_TAG
                )
            }
        } catch (e: Exception) {
            Timber.e("Exception ${e.localizedMessage}")
        }
    }


    private fun showCancelScreenerQuestionnaireAlertDialog() {

    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            showCancelScreenerQuestionnaireAlertDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val QUESTIONNAIRE_FILE_PATH_KEY = "questionnaire-file-path-key"
        const val QUESTIONNAIRE_FRAGMENT_TAG = "questionnaire-fragment-tag"
    }
}