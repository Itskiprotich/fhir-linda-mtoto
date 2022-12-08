package com.imeja.milk_bank.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.imeja.milk_bank.databinding.PatientListBinding
import com.imeja.milk_bank.holders.PatientItemViewHolder
import com.imeja.milk_bank.models.PatientItem
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class PatientItemRecyclerViewAdapter(
    private var patientList: ArrayList<PatientItem>,
    private val onItemClicked: (PatientItem) -> Unit
) : ListAdapter<PatientItem, PatientItemViewHolder>(PatientsItemDiffCallback()) {
    class PatientsItemDiffCallback : DiffUtil.ItemCallback<PatientItem>() {
        override fun areItemsTheSame(
            oldItem: PatientItem,
            newItem: PatientItem
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: PatientItem,
            newItem: PatientItem
        ): Boolean = oldItem.name == newItem.name
    }

    var patientFList = ArrayList<PatientItem>()

    init {
        patientFList = patientList
    }


    override fun getItemCount(): Int {
        return patientFList.size

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientItemViewHolder {
        return PatientItemViewHolder(
            PatientListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PatientItemViewHolder, position: Int) {
        val item =currentList[position]
        holder.bindTo(item,onItemClicked)
    }

}