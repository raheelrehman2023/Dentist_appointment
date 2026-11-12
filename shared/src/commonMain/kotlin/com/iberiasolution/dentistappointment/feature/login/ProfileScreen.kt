package com.iberiasolution.dentistappointment.feature.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iberiasolution.dentistappointment.data.Patient

@Composable
fun ProfileScreen(
    patient: Patient?,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("Back")
        }

        Text(
            text = "My Profile",
            style = MaterialTheme.typography.headlineMedium
        )

        if (patient == null) {
            Text("Patient profile is not available.")
        } else {
            ProfileItem("First Name", patient.name)
            ProfileItem("Surname", patient.surname)
            ProfileItem("Email", patient.email)
            ProfileItem("Phone", patient.phone)
            ProfileItem("Age", patient.age.toString())
            ProfileItem("Sex", patient.sex)

            ProfileItem(
                "Diabetes",
                if (patient.isDiabetic) "Yes" else "No"
            )

            ProfileItem(
                "High Blood Pressure",
                if (patient.hasHighBloodPressure) "Yes" else "No"
            )

            ProfileItem(
                "Pregnancy",
                when (patient.isPregnant) {
                    true -> "Yes"
                    false -> "No"
                    null -> "Not applicable"
                }
            )
        }
    }
}

@Composable
private fun ProfileItem(
    label: String,
    value: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )

        HorizontalDivider()
    }
}
