package com.iberiasolution.dentistappointment.data

import kotlinx.serialization.Serializable

@Serializable
data class Patient(
    val uid: String = "",
    val phone: String = "",
    val createdAt: Long = 0L,
    val name: String,
    val surname: String,
    val email: String,
//    val password: String,
    val sex: String,
    val age: Int,
    val isDiabetic: Boolean,
    val hasHighBloodPressure: Boolean,
    var isPregnant: Boolean?,
    val photoUrl: String = ""
)
