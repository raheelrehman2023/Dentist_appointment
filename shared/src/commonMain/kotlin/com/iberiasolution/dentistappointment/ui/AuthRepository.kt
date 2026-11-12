package com.iberiasolution.dentistappointment.ui

import com.iberiasolution.dentistappointment.data.Patient
import com.iberiasolution.dentistappointment.data.PatientSignupData
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.database


class AuthRepository {

    private val auth get() = Firebase.auth

    private val database
        get() = Firebase.database(
            "https://vivo-v21-pro-default-rtdb.firebaseio.com/"
        )

    // ==========================================
    // 1. PATIENT SIGNUP
    // ==========================================
    suspend fun signUp(
        signupData: PatientSignupData
    ): Result<Unit> {

        return try {
            auth.createUserWithEmailAndPassword(
                email = signupData.email.trim(),
                password = signupData.password
            )

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==========================================
    // 2. PATIENT LOGIN
    // ==========================================
    suspend fun login(
        email: String,
        password: String
    ): Result<Patient?> {

        return try {
            val authResult = auth.signInWithEmailAndPassword(
                email = email.trim(),
                password = password
            )

            val uid = authResult.user?.uid
                ?: throw Exception("Unable to retrieve patient UID")

            val patient = getPatient(uid)

            Result.success(patient)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==========================================
    // 3. GET PATIENT DETAILS
    // ==========================================

    suspend fun getPatient(uid: String): Patient? {
        val snapshot = database
            .reference("patient/$uid")
            .get()

        if (!snapshot.exists) {
            return null
        }

        return snapshot.value<Patient>()
    }



    suspend fun savePatientProfile(patient: Patient): Result<Patient> {
        return try {
            // Get the currently authenticated patient's UID
            val uid = auth.currentUser?.uid
                ?: throw Exception("No authenticated patient found")

            // Ensure the profile uses the Firebase Authentication UID
            val patientToSave = patient.copy(uid = uid)

            // Save under patient/{uid}
            database.reference("patient/$uid")
                .setValue(patientToSave)

            Result.success(patientToSave)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    // ==========================================
    // 4. LOGOUT
    // ==========================================
    suspend fun logout() {
        auth.signOut()
    }

    // ==========================================
    // 5. FORGOT PASSWORD
    // ==========================================
    suspend fun resetPassword(email: String): Result<Unit> {

        return try {
            auth.sendPasswordResetEmail(email.trim())
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
