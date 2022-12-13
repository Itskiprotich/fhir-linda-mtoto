package com.imeja.milk_bank.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.get
import com.google.android.fhir.search.*
import com.imeja.milk_bank.models.PatientItem
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.RiskAssessment
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PatientListViewModel(application: Application, private val fhirEngine: FhirEngine) :
    AndroidViewModel(application) {

    val liveSearchedPatients = MutableLiveData<List<PatientItem>>()
    val dischargedPatients = MutableLiveData<List<PatientItem>>()
    val patientCount = MutableLiveData<Long>()

    init {
        updatePatientListAndPatientCount(
            { getSearchResults() },
            { getDischargeResults() },
            { count() })
    }

    fun searchPatientsByName(nameQuery: String) {
        updatePatientListAndPatientCount(
            { getSearchResults(nameQuery) },
            { getDischargeResults(nameQuery) },
            { count(nameQuery) })
    }

    /**
     * [updatePatientListAndPatientCount] calls the search and count lambda and updates the live data
     * values accordingly. It is initially called when this [ViewModel] is created. Later its called
     * by the client every time search query changes or data-sync is completed.
     */
    private fun updatePatientListAndPatientCount(
        search: suspend () -> List<PatientItem>,
        searchDischarged: suspend () -> List<PatientItem>,
        count: suspend () -> Long
    ) {
        viewModelScope.launch {
            liveSearchedPatients.value = search()
            dischargedPatients.value = searchDischarged()
            patientCount.value = count()
        }
    }

    /**
     * Returns count of all the [Patient] who match the filter criteria unlike [getSearchResults]
     * which only returns a fixed range.
     */
    private suspend fun count(nameQuery: String = ""): Long {
        val total = fhirEngine.count<Patient> {
            if (nameQuery.isNotEmpty()) {
                filter(
                    Patient.NAME,
                    {
                        modifier = StringFilterModifier.CONTAINS
                        value = nameQuery
                    }
                )
            }
            filterCity(this,true)
        }

        Timber.e("Total Patients found $total")
        return total
    }

    private suspend fun getSearchResults(nameQuery: String = ""): List<PatientItem> {
        val patients: MutableList<PatientItem> = mutableListOf()
        fhirEngine
            .search<Patient> {
                if (nameQuery.isNotEmpty()) {
                    filter(
                        Patient.NAME,
                        {
                            modifier = StringFilterModifier.CONTAINS
                            value = nameQuery
                        }
                    )
                }
                filterCity(this,true)
                sort(Patient.GIVEN, Order.ASCENDING)
                count = 100
                from = 0
            }
            .mapIndexed { index, fhirPatient -> fhirPatient.toPatientItem(index + 1) }
            .let { patients.addAll(it) }

        return patients
    }

    private suspend fun getDischargeResults(nameQuery: String = ""): List<PatientItem> {
        val patients: MutableList<PatientItem> = mutableListOf()
        fhirEngine
            .search<Patient> {
                if (nameQuery.isNotEmpty()) {
                    filter(
                        Patient.NAME,
                        {
                            modifier = StringFilterModifier.CONTAINS
                            value = nameQuery
                        }
                    )
                }
                filterCity(this,false)
                sort(Patient.GIVEN, Order.ASCENDING)
                count = 100
                from = 0
            }
            .mapIndexed { index, fhirPatient -> fhirPatient.toPatientItem(index + 1) }
            .let { patients.addAll(it) }

        return patients
    }

    private fun filterCity(search: Search,status:Boolean) {
        search.filter(Patient.ADDRESS_CITY, { value = "NAIROBI" })
        search.filter(Patient.ACTIVE, {
            value = of(boolean = status)
        })
    }



    private suspend fun getRiskAssessments(): Map<String, RiskAssessment?> {
        return fhirEngine.search<RiskAssessment> {}.groupBy { it.subject.reference }
            .mapValues { entry
                ->
                entry
                    .value
                    .filter { it.hasOccurrence() }
                    .sortedByDescending { it.occurrenceDateTimeType.value }
                    .firstOrNull()
            }
    }

      fun dischargePatient(resourceId: String) {
          viewModelScope.launch {
              val patient = fhirEngine.get<Patient>(resourceId)
              if (patient.active) {
                  patient.active = false
                  fhirEngine.update(patient)

              }

          }

    }


    /** The Patient's details for display purposes. */


    /** The Observation's details for display purposes. */
    data class ObservationItem(
        val id: String,
        val code: String,
        val effective: String,
        val value: String
    ) {
        override fun toString(): String = code
    }

    data class ConditionItem(
        val id: String,
        val code: String,
        val effective: String,
        val value: String
    ) {
        override fun toString(): String = code
    }

    class PatientListViewModelFactory(
        private val application: Application,
        private val fhirEngine: FhirEngine
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PatientListViewModel::class.java)) {
                return PatientListViewModel(application, fhirEngine) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

internal fun Patient.toPatientItem(position: Int): PatientItem {
    // Show nothing if no values available for gender and date of birth.
    val patientId = if (hasIdElement()) idElement.idPart else ""
    val name = if (hasName()) name[0].nameAsSingleString else ""
    val gender = if (hasGenderElement()) genderElement.valueAsString else ""
    val dob =
        if (hasBirthDateElement())
            LocalDate.parse(birthDateElement.valueAsString, DateTimeFormatter.ISO_DATE)
        else null
    val phone = if (hasTelecom()) telecom[0].value else ""
    val city = if (hasAddress()) address[0].city else ""
    val country = if (hasAddress()) address[0].country else ""
    val isActive = active
    val html: String = if (hasText()) text.div.valueAsString else ""

    return PatientItem(
        id = position.toString(),
        resourceId = patientId,
        name = name,
        gender = gender ?: "",
        dob = dob,
        phone = phone ?: "",
        city = city ?: "",
        country = country ?: "",
        isActive = isActive,
        html = html
    )
}
