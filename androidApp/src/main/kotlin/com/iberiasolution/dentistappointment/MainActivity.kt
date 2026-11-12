package com.iberiasolution.dentistappointment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.iberiasolution.dentistappointment.shared.data.PatientLocalDataSource

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val dataStore = createPatientDataStore(applicationContext)
        val patientLocalDataSource = PatientLocalDataSource(dataStore)

        setContent {
            App(
                patientLocalDataSource = patientLocalDataSource
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(
        patientLocalDataSource = TODO()
    )
}