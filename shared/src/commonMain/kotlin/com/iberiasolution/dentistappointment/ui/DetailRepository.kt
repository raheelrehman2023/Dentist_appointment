package com.iberiasolution.dentistappointment.ui

import kotlinx.coroutines.delay

data class DentistDetail(
    val id: Int,
    val name: String,
    val speciality: String,
    val rating: String,
    val experience: String,
    val patients: String,
    val about: String,
    val services: List<String>,
    val timing: String,
    val location: String,
    val fee: String
)

class DetailRepository {

    // suspend = takes time, must be in coroutine
    suspend fun getDentistById(id: Int): DentistDetail {
        delay(800) // Fake internet delay

        // Later real Firebase:
        // val doc = Firebase.firestore.collection("dentists").document(id.toString()).get()
        // return DentistDetail(... from doc ...)

        // For now, return fake data based on id
        // In real app, id decides which dentist to return
        return when (id) {
            1 -> DentistDetail(
                id = 1,
                name = "Maria Santos",
                speciality = "Orthodontist",
                rating = "4.9",
                experience = "8",
                patients = "1200+",
                about = "Dr. Maria is a specialist in braces and aligners. She is known for painless treatment and friendly approach with kids and adults.",
                services = listOf("Braces", "Clear Aligners", "Teeth Straightening", "Retainers"),
                timing = "Mon - Fri: 09:00 - 17:00",
                location = "Castelo Branco, Rua da Sé 12",
                fee = "40"
            )
            2 -> DentistDetail(
                id = 2,
                name = "João Pereira",
                speciality = "Implantologist",
                rating = "4.8",
                experience = "12",
                patients = "2000+",
                about = "Dr. João specializes in dental implants and full mouth reconstruction with over 12 years of experience.",
                services = listOf("Dental Implants", "Bone Grafting", "Full Mouth Rehab", "Crowns"),
                timing = "Mon - Sat: 10:00 - 18:00",
                location = "Castelo Branco, Av. Liberdade 45",
                fee = "60"
            )
            else -> DentistDetail(
                id = id,
                name = "Ana Costa",
                speciality = "General Dentist",
                rating = "4.7",
                experience = "5",
                patients = "800+",
                about = "Dr. Ana provides general dentistry, cleaning and whitening for the whole family.",
                services = listOf("Cleaning", "Whitening", "Fillings", "Root Canal"),
                timing = "Tue - Sat: 09:00 - 16:00",
                location = "Castelo Branco, Rua Nova 8",
                fee = "30"
            )
        }
    }
}