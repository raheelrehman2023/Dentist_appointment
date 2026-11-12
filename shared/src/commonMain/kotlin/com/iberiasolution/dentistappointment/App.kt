package com.iberiasolution.dentistappointment

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.iberiasolution.dentistappointment.data.Patient
import com.iberiasolution.dentistappointment.feature.login.BookingScreen
import com.iberiasolution.dentistappointment.feature.login.DetailScreen
import com.iberiasolution.dentistappointment.feature.login.HomeScreen
import com.iberiasolution.dentistappointment.feature.login.LoginScreen
import com.iberiasolution.dentistappointment.feature.login.SignupScreen
import com.iberiasolution.dentistappointment.themes.AppTheme
import com.iberiasolution.dentistappointment.shared.data.PatientLocalDataSource
import androidx.compose.runtime.collectAsState
import com.iberiasolution.dentistappointment.ui.AuthRepository
import com.iberiasolution.dentistappointment.data.AuthViewModel
import com.iberiasolution.dentistappointment.feature.login.ProfileCompletionScreen
import com.iberiasolution.dentistappointment.feature.login.ProfileScreen
import kotlinx.coroutines.launch


@Composable
fun App(
    patientLocalDataSource: PatientLocalDataSource

) {
    AppTheme {
        val scope = rememberCoroutineScope()
        val authRepository = remember { AuthRepository() }

        val authViewModel = remember {
            AuthViewModel(
                repository = authRepository,
                scope = scope
            )
        }
        val localPatient by patientLocalDataSource.patient.collectAsState(
            initial = null
        )
        val authState by authViewModel.uiState.collectAsState()

        // ==================== STATE ====================
        // 1. BACK STACK - rememberSaveable survives rotation
        // Last item = current screen, like FragmentManager back stack
        var backStack by rememberSaveable { mutableStateOf(listOf("login")) }
        val currentScreen = backStack.last()

        // 2. DATA PASSING - like Intent putExtra, but survives rotation
        // Holds which dentist was clicked on Home, to send to Detail & Booking
        var selectedDentistId by rememberSaveable { mutableStateOf(0) }

        // 3. DIALOG STATE - normal remember is enough
        var showLogoutDialog by remember { mutableStateOf(false) }

        // 4. DOUBLE BACK PRESS - for Login screen to exit app
        var lastBackPressTime by remember { mutableStateOf(0L) }
        var showExitToast by remember { mutableStateOf(false) }

        var isLoading by remember { mutableStateOf(false) }
        var authError by remember { mutableStateOf<String?>(null) }
        var currentPatient by remember { mutableStateOf<Patient?>(null) }

        // Signup information to prefill profile completion
        var pendingPatientName by rememberSaveable {
            mutableStateOf("")
        }

        var pendingPatientEmail by rememberSaveable {
            mutableStateOf("")
        }

// Profile saving state
        var isSavingProfile by remember {
            mutableStateOf(false)
        }

        var profileSaveError by remember {
            mutableStateOf<String?>(null)
        }


        // ==================== NAVIGATION HELPERS ====================
        // Like FragmentTransaction.replace() + addToBackStack()
        fun navigateTo(screen: String) {
            backStack = backStack + screen // PUSH
        }

        // Like FragmentManager.popBackStack()
        fun goBack() {
            if (backStack.size > 1) {
                backStack = backStack.dropLast(1) // POP
            }
        }

        // Clear everything and go to one screen - used after Login / Logout / Booking
        fun navigateAndClear(screen: String) {
            backStack = listOf(screen)
        }

        fun logout() {
            showLogoutDialog = false
            selectedDentistId = 0

            scope.launch {
                authViewModel.logout()
                patientLocalDataSource.clearPatient()
                currentPatient = null
            }

            navigateAndClear("login")
        }

        // ==================== BACK BUTTON HANDLER ====================
        // Intercepts Android Back Button
        BackHandler(enabled = true) {
            when (currentScreen) {
                "home" -> {
                    // On Home -> Don't pop to Login, show Logout warning
                    showLogoutDialog = true
                }

                "login" -> {
                    // On Login -> Double press to exit
                    val now = currentTimeMillis()
                    if (now - lastBackPressTime < 2000) {
                        // Pressed twice within 2 sec -> Exit app
                        // On Android, App.kt caller will handle finish()
                        // For now, we just hide - system will exit
                        // If you want real exit, use: (LocalContext.current as Activity).finish()
                    } else {
                        lastBackPressTime = now
                        showExitToast = true // Show "Press again to exit"
                    }
                }

                "signup" -> {
                    // On Signup -> Go back to Login normally
                    goBack()
                }

                else -> {
                    // Detail, Booking, etc -> Normal back
                    goBack()
                }
            }
        }



            // ==================== SCREENS ====================
            when (currentScreen) {
                "profile-completion" -> ProfileCompletionScreen(
                    name = pendingPatientName,
                    email = pendingPatientEmail,
                    isSaving = isSavingProfile,
                    errorMessage = profileSaveError,
                    onSaveProfile = { patient ->
                        isSavingProfile = true
                        profileSaveError = null

                        scope.launch {
                            val result = authRepository.savePatientProfile(patient)

                            result.onSuccess { savedPatient ->
                                patientLocalDataSource.savePatient(savedPatient)
                                currentPatient = savedPatient
                                isSavingProfile = false
                                navigateAndClear("home")
                            }.onFailure { error ->
                                profileSaveError = error.message
                                    ?: "Unable to save your profile."
                                isSavingProfile = false
                            }
                        }
                    }
                )

                "login" -> LoginScreen(
                    onLoginClick = { email, password ->
                        authViewModel.clearMessages()
                        authViewModel.login(email, password)
                    },
                    onForgotPasswordClick = {
                        // We'll connect password reset next.
                    },
                    onSignUpClick = {
                        authViewModel.clearMessages()
                        navigateTo("signup")
                    }
                )

                "signup" -> SignupScreen(
                    onSignUpClick = { signupData ->
                        pendingPatientName = signupData.name
                        pendingPatientEmail = signupData.email

                        profileSaveError = null
                        authViewModel.signUp(signupData)
                    },
                    onSignupSuccess = {
                        authViewModel.clearSuccess()
                        authViewModel.clearMessages()
                        navigateAndClear("login")
                    },
                    onBackToLoginClick = {
                        authViewModel.clearSuccess()
                        authViewModel.clearMessages()
                        goBack()
                    },
                    viewModel = authViewModel
                )

                "home" -> HomeScreen(
                    onDentistClick = { dentistId ->
                        // putExtra equivalent
                        selectedDentistId = dentistId
                        navigateTo("detail")
                    },
                    onLogoutClick = {
                        showLogoutDialog = true
                    },
                    onProfileClick = {
                        navigateTo("profile")
                    }
                )

                "detail" -> DetailScreen(
                    dentistId = selectedDentistId, // getExtra equivalent
                    onBackClick = {
                        goBack() // Pop Detail -> Show Home
                    },
                    onBookClick = {
                        navigateTo("booking") // Push Booking
                    }
                )

                "booking" -> BookingScreen(
                    dentistId = selectedDentistId,
                    onBackClick = {
                        goBack() // Pop Booking -> Show Detail
                    },
                    onConfirmClick = {
                        // Booking success -> Clear Detail + Booking, go to Home
                        // Stack was ["home", "detail", "booking"]
                        // Now: ["home"]
                        navigateAndClear("home")
                    }
                )
                "profile" -> ProfileScreen(
                    patient = localPatient,
                    onBackClick = {
                        goBack()
                    }
                )


            }

        LaunchedEffect(currentScreen, authState.isSuccess) {

            if (currentScreen == "login" && authState.isSuccess) {

                val loggedInPatient = authState.patient

                if (loggedInPatient != null) {

                    currentPatient = loggedInPatient

                    patientLocalDataSource.savePatient(loggedInPatient)

                    navigateAndClear("home")

                } else {

                    currentPatient = null

                    navigateAndClear("profile-completion")
                }

                authViewModel.clearSuccess()
            }
        }


            // ==================== LOGOUT DIALOG ====================
            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    title = { Text("Logout?") },
                    text = { Text("You are about to logout. Are you sure?") },
                    confirmButton = {
                        TextButton(onClick = { logout() }) {
                            Text("Yes, Logout")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showLogoutDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }


            // ==================== EXIT TOAST ====================
            // Simple toast for double-back-press on Login
        }
    }
