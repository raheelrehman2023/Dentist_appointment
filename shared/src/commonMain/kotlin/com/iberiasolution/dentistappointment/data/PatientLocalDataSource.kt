
package com.iberiasolution.dentistappointment.shared.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.iberiasolution.dentistappointment.data.Patient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PatientLocalDataSource(
    private val dataStore: DataStore<Preferences>
) {

    private object Keys {
        val uid = stringPreferencesKey("uid")
        val name = stringPreferencesKey("name")
        val surname = stringPreferencesKey("surname")
        val email = stringPreferencesKey("email")
        val phone = stringPreferencesKey("phone")
        val createdAt = longPreferencesKey("createdAt")
        val sex = stringPreferencesKey("sex")
        val age = intPreferencesKey("age")
        val isDiabetic = booleanPreferencesKey("isDiabetic")
        val hasHighBloodPressure =
            booleanPreferencesKey("hasHighBloodPressure")
        val isPregnant = booleanPreferencesKey("isPregnant")
        val photoUrl = stringPreferencesKey("photoUrl")
    }

    // Save or replace the locally stored patient profile
    suspend fun savePatient(patient: Patient) {
        dataStore.edit { preferences ->
            preferences[Keys.uid] = patient.uid
            preferences[Keys.name] = patient.name
            preferences[Keys.surname] = patient.surname
            preferences[Keys.email] = patient.email
            preferences[Keys.phone] = patient.phone
            preferences[Keys.createdAt] = patient.createdAt
            preferences[Keys.sex] = patient.sex
            preferences[Keys.age] = patient.age
            preferences[Keys.isDiabetic] = patient.isDiabetic
            preferences[Keys.hasHighBloodPressure] =
                patient.hasHighBloodPressure
            preferences[Keys.photoUrl] = patient.photoUrl

            val isPregnant = patient.isPregnant

            if (isPregnant == null) {
                preferences.remove(Keys.isPregnant)
            } else {
                preferences[Keys.isPregnant] = isPregnant
            }
        }
    }

    // Observe the locally stored patient profile
    val patient: Flow<Patient?> = dataStore.data.map { preferences ->

        val uid = preferences[Keys.uid]

        if (uid.isNullOrBlank()) {
            null
        } else {
            Patient(
                uid = uid,
                name = preferences[Keys.name] ?: "",
                surname = preferences[Keys.surname] ?: "",
                email = preferences[Keys.email] ?: "",
                phone = preferences[Keys.phone] ?: "",
                createdAt = preferences[Keys.createdAt] ?: 0L,
                sex = preferences[Keys.sex] ?: "",
                age = preferences[Keys.age] ?: 0,
                isDiabetic = preferences[Keys.isDiabetic] ?: false,
                hasHighBloodPressure =
                    preferences[Keys.hasHighBloodPressure] ?: false,
                isPregnant = preferences[Keys.isPregnant],
                photoUrl = preferences[Keys.photoUrl] ?: ""
            )
        }
    }

    // Clear the locally stored patient profile on logout
    suspend fun clearPatient() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
