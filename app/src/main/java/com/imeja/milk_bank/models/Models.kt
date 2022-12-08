package com.imeja.milk_bank.models

import java.time.LocalDate

class Models {
}

data class PatientItem(
    val id: String,
    val resourceId: String,
    val name: String,
    val gender: String,
    val dob: LocalDate? = null,
    val phone: String,
    val city: String,
    val country: String,
    val isActive: Boolean,
    val html: String,
    var risk: String? = "",
) {
    override fun toString(): String = name
}