package com.iberiasolution.dentistappointment

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import java.io.File

fun createPatientDataStore(
    context: Context
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            File(
                context.filesDir,
                "patient.preferences_pb"
            ).absolutePath.toPath()
        }
    )
}