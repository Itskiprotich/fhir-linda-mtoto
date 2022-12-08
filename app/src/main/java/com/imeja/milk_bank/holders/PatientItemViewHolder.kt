package com.imeja.milk_bank.holders

import android.content.res.ColorStateList
import android.content.res.Resources
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.imeja.milk_bank.R
import com.imeja.milk_bank.databinding.PatientListBinding
import com.imeja.milk_bank.models.PatientItem
import com.imeja.milk_bank.viewmodels.PatientListViewModel
import timber.log.Timber
import java.time.LocalDate
import java.time.Period

class PatientItemViewHolder(binding: PatientListBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val statusView: ImageView = binding.status
    private val nameView: TextView = binding.name
    private val ageView: TextView = binding.fieldName
    private val idView: TextView = binding.id

    fun bindTo(
        patientItem: PatientItem,
        onItemClicked: (PatientItem) -> Unit
    ) {
        this.nameView.text = patientItem.name
        this.ageView.text = getFormattedAge(patientItem, ageView.context.resources)
        this.idView.text = "Id: #---${getTruncatedId(patientItem)}"
        this.itemView.setOnClickListener { onItemClicked(patientItem) }
    }

    private fun getFormattedAge(
        patientItem: PatientItem,
        resources: Resources
    ): String {
        if (patientItem.dob == null) return ""
        return Period.between(patientItem.dob, LocalDate.now()).let {
            when {
                it.years > 0 -> resources.getQuantityString(R.plurals.ageYear, it.years, it.years)
                it.months > 0 -> resources.getQuantityString(R.plurals.ageMonth, it.months, it.months)
                else -> resources.getQuantityString(R.plurals.ageDay, it.days, it.days)
            }
        }
    }

    /** The new ui just shows shortened id with just last 3 characters. */
    private fun getTruncatedId(patientItem: PatientItem): String {
        return patientItem.resourceId.takeLast(3)
    }
}
