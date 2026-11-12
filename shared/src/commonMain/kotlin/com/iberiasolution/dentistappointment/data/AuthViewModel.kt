package com.iberiasolution.dentistappointment.data

import com.iberiasolution.dentistappointment.ui.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update


data class AuthUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val patient: Patient? = null,
    val error: String? = null,
    val message: String? = null
)

class AuthViewModel(
    private val repository: AuthRepository,
    private val scope: CoroutineScope
) {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // SIGN UP
    fun signUp(signupData: PatientSignupData) {
        scope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isSuccess = false,
                    patient = null,
                    error = null,
                    message = null
                )
            }

            val result = repository.signUp(signupData)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isSuccess = result.isSuccess,
                    patient = null,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }

    // LOGIN
    fun login(email: String, password: String) {
        scope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isSuccess = false,
                    patient = null,
                    error = null,
                    message = null
                )
            }

            val result = repository.login(email, password)
            println("LOGIN SUCCESS: ${result.isSuccess}")
            println("LOGIN PATIENT: ${result.getOrNull()}")
            println("LOGIN ERROR: ${result.exceptionOrNull()?.message}")

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isSuccess = result.isSuccess,
                    patient = result.getOrNull(),
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }

    // RESET PASSWORD
    fun resetPassword(email: String) {
        scope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    message = null
                )
            }

            val result = repository.resetPassword(email)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    message = if (result.isSuccess) {
                        "Password reset email sent successfully."
                    } else null,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }

    // LOGOUT
    fun logout() {
        scope.launch {
            try {
                repository.logout()
                _uiState.value = AuthUiState()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message)
                }
            }
        }
    }

    // CLEAR MESSAGES
    fun clearMessages() {
        _uiState.update {
            it.copy(
                error = null,
                message = null
            )
        }
    }

    // RESET SUCCESS STATE AFTER NAVIGATION
    fun clearSuccess() {
        _uiState.update {
            it.copy(isSuccess = false)
        }
    }
}