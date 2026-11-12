package com.iberiasolution.dentistappointment.ui

import kotlinx.coroutines.delay

data class Dentist(
    val id: Int,
    val name: String,
    val speciality: String,
    val fee: String
)

class BookingRepository {

    // suspend = takes time, must be called in coroutine
    suspend fun getDentistById(id: Int): Dentist {
        delay(800) // Fake internet delay

        // Later replace with real Firebase:
        // val doc = Firebase.firestore.collection("dentists").document(id.toString()).get()
        // return Dentist(...)

        return Dentist(
            id = id,
            name = "Maria Santos",
            speciality = "Orthodontist • 8 yrs exp",
            fee = "40"
        )
    }

    suspend fun bookAppointment(
        dentistId: Int,
        date: String,
        time: String,
        reason: String
    ) {
        delay(1000) // Fake save

        // Later real Firebase:
        // Firebase.firestore.collection("appointments").add(
        //   mapOf("dentistId" to dentistId, "date" to date, "time" to time, "reason" to reason)
        // )
    }
}