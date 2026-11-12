package com.iberiasolution.dentistappointment.feature.login


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iberiasolution.dentistappointment.data.DetailViewModel

@Composable
fun DetailScreen(
    dentistId: Int,
    onBackClick: () -> Unit,
    onBookClick: () -> Unit,
    viewModel: DetailViewModel = DetailViewModel()
) {
    // OBSERVER: Watch ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // When screen opens, load dentist once
    LaunchedEffect(dentistId) {
        viewModel.loadDentist(dentistId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dentist Details") },
                navigationIcon = {
                    TextButton(onClick = onBackClick) { Text("← Back") }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            // Loading state
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            // Error state
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    Text(uiState.error!!)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadDentist(dentistId) }) {
                        Text("Try Again")
                    }
                }
            }
        } else {
            // Success state
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // --- HEADER CARD ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Dr. ${uiState.name}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            uiState.speciality,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AssistChip(onClick = {}, label = { Text("⭐ ${uiState.rating}") })
                            AssistChip(onClick = {}, label = { Text("${uiState.experience} yrs exp") })
                            AssistChip(onClick = {}, label = { Text("${uiState.patients} patients") })
                        }
                    }
                }

                // --- ABOUT ---
                Text("About", style = MaterialTheme.typography.titleMedium)
                Text(uiState.about, style = MaterialTheme.typography.bodyMedium)

                // --- SERVICES ---
                Text("Services", style = MaterialTheme.typography.titleMedium)
                uiState.services.forEach { service ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("•  ", style = MaterialTheme.typography.bodyMedium)
                        Text(service, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                // --- TIMING ---
                Text("Available", style = MaterialTheme.typography.titleMedium)
                Text(uiState.timing, style = MaterialTheme.typography.bodyMedium)
                Text(uiState.location, style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.weight(1f))

                // --- PRICE + BOOK ROW ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Column {
                        Text("Consultation Fee", style = MaterialTheme.typography.bodySmall)
                        Text(
                            "${uiState.fee} €",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    Button(
                        onClick = onBookClick,
                        modifier = Modifier.height(50.dp).width(160.dp)
                    ) {
                        Text("Book Now")
                    }
                }
            }
        }
    }
}