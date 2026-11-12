package com.iberiasolution.dentistappointment.feature.login


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iberiasolution.dentistappointment.data.BookingViewModel

@Composable
fun BookingScreen(
    dentistId: Int,
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit,
    viewModel: BookingViewModel = BookingViewModel()
) {
    // OBSERVER: Watch ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // When screen opens, load dentist info once
    LaunchedEffect(dentistId) {
        viewModel.loadDentist(dentistId)
    }

    // If booking succeeded, tell App.kt to go back to Home
    LaunchedEffect(uiState.isBooked) {
        if (uiState.isBooked) {
            onConfirmClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Appointment") },
                navigationIcon = {
                    TextButton(onClick = onBackClick) { Text("← Back") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Dentist Info
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Text("Dr. ${uiState.dentistName}", style = MaterialTheme.typography.titleLarge)
                Text(uiState.dentistSpeciality, style = MaterialTheme.typography.bodyMedium)
                Text("Fee: ${uiState.fee} €", style = MaterialTheme.typography.bodyMedium)
            }

            Divider()

            // --- SELECT DATE ---
            Text("Select Date", style = MaterialTheme.typography.titleMedium)

            // Simple date chips - in real app use DatePicker
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Today", "Tomorrow", "Mon 10", "Tue 11").forEach { date ->
                    FilterChip(
                        selected = uiState.selectedDate == date,
                        onClick = { viewModel.onDateSelected(date) },
                        label = { Text(date) }
                    )
                }
            }

            // --- SELECT TIME ---
            Text("Select Time", style = MaterialTheme.typography.titleMedium)

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(180.dp)
            ) {
                items(uiState.availableTimes) { time ->
                    FilterChip(
                        selected = uiState.selectedTime == time,
                        onClick = { viewModel.onTimeSelected(time) },
                        label = { Text(time) }
                    )
                }
            }

            // --- REASON ---
            Text("Reason for Visit", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = uiState.reason,
                onValueChange = { viewModel.onReasonChanged(it) },
                placeholder = { Text("e.g. Tooth pain, Cleaning...") },
                modifier = Modifier.fillMaxWidth()
            )

            // Error message
            if (uiState.error != null) {
                Text(uiState.error!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- CONFIRM BUTTON ---
            Button(
                onClick = { viewModel.onConfirmClicked() },
                enabled = !uiState.isBooking, // Disable while booking
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                if (uiState.isBooking) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Confirm Booking - ${uiState.fee} €")
                }
            }
        }
    }
}