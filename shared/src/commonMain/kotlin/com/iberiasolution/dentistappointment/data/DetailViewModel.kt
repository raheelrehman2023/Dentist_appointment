package com.iberiasolution.dentistappointment.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberiasolution.dentistappointment.ui.DetailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Everything UI needs
data class DetailUiState(
    val name: String = "",
    val speciality: String = "",
    val rating: String = "",
    val experience: String = "",
    val patients: String = "",
    val about: String = "",
    val services: List<String> = emptyList(),
    val timing: String = "",
    val location: String = "",
    val fee: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class DetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState

    private val repository = DetailRepository()

    fun loadDentist(dentistId: Int) {
        viewModelScope.launch { // Coroutine - Firebase takes time
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val dentist = repository.getDentistById(dentistId)
                _uiState.value = _uiState.value.copy(
                    name = dentist.name,
                    speciality = dentist.speciality,
                    rating = dentist.rating,
                    experience = dentist.experience,
                    patients = dentist.patients,
                    about = dentist.about,
                    services = dentist.services,
                    timing = dentist.timing,
                    location = dentist.location,
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
}