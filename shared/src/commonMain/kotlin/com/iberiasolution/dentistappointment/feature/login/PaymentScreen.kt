package com.iberiasolution.dentistappointment.feature.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// --- STATE ---
data class PaymentUiState(
    val cardNumber: String = "",
    val cardName: String = "",
    val expiry: String = "",
    val cvv: String = "",
    val isPaying: Boolean = false,
    val isPaid: Boolean = false,
    val error: String? = null
)

class PaymentViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState

    fun onCardNumberChanged(value: String) {
        // Only numbers, max 16 digits
        if (value.length <= 16 && value.all { it.isDigit() }) {
            _uiState.value = _uiState.value.copy(cardNumber = value, error = null)
        }
    }
    fun onCardNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(cardName = value)
    }
    fun onExpiryChanged(value: String) {
        if (value.length <= 5) _uiState.value = _uiState.value.copy(expiry = value)
    }
    fun onCvvChanged(value: String) {
        if (value.length <= 3 && value.all { it.isDigit() }) {
            _uiState.value = _uiState.value.copy(cvv = value)
        }
    }

    fun onPayClicked(amount: String) {
        val s = _uiState.value
        if (s.cardNumber.length < 16) {
            _uiState.value = s.copy(error = "Enter 16 digit card number")
            return
        }
        if (s.cardName.isBlank()) {
            _uiState.value = s.copy(error = "Enter name on card")
            return
        }
        if (s.expiry.length < 5) {
            _uiState.value = s.copy(error = "Enter expiry MM/YY")
            return
        }
        if (s.cvv.length < 3) {
            _uiState.value = s.copy(error = "Enter 3 digit CVV")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isPaying = true, error = null)
            delay(1500) // Fake payment gateway delay
            // Later real: Stripe / Razorpay / Firebase Function here
            _uiState.value = _uiState.value.copy(isPaying = false, isPaid = true)
        }
    }
}

// --- UI ---
@Composable
fun PaymentScreen(
    dentistName: String,
    amount: String,
    date: String,
    time: String,
    onBackClick: () -> Unit,
    onPaymentSuccess: () -> Unit,
    viewModel: PaymentViewModel = PaymentViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // When payment succeeds, tell App.kt
    LaunchedEffect(uiState.isPaid) {
        if (uiState.isPaid) onPaymentSuccess()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment") },
                navigationIcon = { TextButton(onClick = onBackClick) { Text("← Back") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- BOOKING SUMMARY CARD ---
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Dr. $dentistName", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("$date at $time")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Total to Pay: $amount €", style = MaterialTheme.typography.headlineSmall)
                }
            }

            Text("Card Details", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = uiState.cardNumber,
                onValueChange = { viewModel.onCardNumberChanged(it) },
                label = { Text("Card Number") },
                placeholder = { Text("1234 5678 9012 3456") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.cardName,
                onValueChange = { viewModel.onCardNameChanged(it) },
                label = { Text("Name on Card") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = uiState.expiry,
                    onValueChange = { viewModel.onExpiryChanged(it) },
                    label = { Text("Expiry MM/YY") },
                    placeholder = { Text("12/28") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = uiState.cvv,
                    onValueChange = { viewModel.onCvvChanged(it) },
                    label = { Text("CVV") },
                    placeholder = { Text("123") },
                    modifier = Modifier.weight(1f)
                )
            }

            if (uiState.error != null) {
                Text(uiState.error!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.onPayClicked(amount) },
                enabled = !uiState.isPaying,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                if (uiState.isPaying) CircularProgressIndicator(modifier = Modifier.size(20.dp))
                else Text("Pay $amount €")
            }
            Text(
                "This is a test payment. No real money will be taken.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}