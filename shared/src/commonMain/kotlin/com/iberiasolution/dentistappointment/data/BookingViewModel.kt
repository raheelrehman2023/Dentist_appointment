package com.iberiasolution.dentistappointment.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberiasolution.dentistappointment.ui.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Everything UI needs, in one place
data class BookingUiState(
    val dentistName: String = "",
    val dentistSpeciality: String = "",
    val fee: String = "",
    val selectedDate: String = "Today",
    val selectedTime: String? = null,
    val availableTimes: List<String> = listOf(
        "09:00", "10:00", "11:00", "14:00", "15:00", "16:00", "17:00"
    ),
    val reason: String = "",
    val isLoading: Boolean = false,
    val isBooking: Boolean = false,
    val isBooked: Boolean = false,
    val error: String? = null
)

class BookingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState

    private val repository = BookingRepository()

    // Called from LaunchedEffect when screen opens
    fun loadDentist(dentistId: Int) {
        viewModelScope.launch { // Coroutine - Firebase takes time
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val dentist = repository.getDentistById(dentistId)
                _uiState.value = _uiState.value.copy(
                    dentistName = dentist.name,
                    dentistSpeciality = dentist.speciality,
                    fee = dentist.fee,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load dentist"
                )
            }
        }
    }

    // onClick - Date
    fun onDateSelected(date: String) {
        _uiState.value = _uiState.value.copy(selectedDate = date, selectedTime = null)
    }

    // onClick - Time
    fun onTimeSelected(time: String) {
        _uiState.value = _uiState.value.copy(selectedTime = time, error = null)
    }

    // onChange - Reason text
    fun onReasonChanged(reason: String) {
        _uiState.value = _uiState.value.copy(reason = reason)
    }

    // onClick - Confirm Button - MAIN LOGIC
    fun onConfirmClicked() {
        val state = _uiState.value

        // 1. Validation - No Firebase yet
        if (state.selectedTime == null) {
            _uiState.value = state.copy(error = "Please select a time")
            return
        }
        if (state.reason.isBlank()) {
            _uiState.value = state.copy(error = "Please enter reason")
            return
        }

        // 2. Save to Firebase via Repository
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isBooking = true, error = null)
            try {
                repository.bookAppointment(
                    dentistId = 1, // In real app pass real id
                    date = state.selectedDate,
                    time = state.selectedTime!!,
                    reason = state.reason
                )
                // 3. Success -> Tell UI to go back to Home
                _uiState.value = _uiState.value.copy(isBooking = false, isBooked = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isBooking = false,
                    error = "Booking failed, try again"
                )
            }
        }
    }
}