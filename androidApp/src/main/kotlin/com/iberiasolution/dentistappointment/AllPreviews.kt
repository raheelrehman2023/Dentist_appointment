package com.iberiasolution.dentistappointment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.viewModelFactory
import com.iberiasolution.dentistappointment.data.AuthViewModel
import com.iberiasolution.dentistappointment.data.Patient
import com.iberiasolution.dentistappointment.feature.login.HomeScreen
import com.iberiasolution.dentistappointment.feature.login.LoginScreen
import com.iberiasolution.dentistappointment.feature.login.ProfileScreen
import com.iberiasolution.dentistappointment.feature.login.SignupScreen
import com.iberiasolution.dentistappointment.themes.AppTheme
import com.iberiasolution.dentistappointment.ui.AuthRepository


// --- LOGIN ---
@Preview(name = "Login", showBackground = true, showSystemUi = true)
@Composable
fun LoginPreview() {
    AppTheme {
        LoginScreen(
            onLoginClick = { email, password -> },
            onForgotPasswordClick = {},
            onSignUpClick = {}
        )
    }
}

@Preview(name = "Signup", showBackground = true, showSystemUi = true)
@Composable
fun SignupPreview() {
    AppTheme {
        val scope = rememberCoroutineScope()

        val viewModel = remember {
            AuthViewModel(
                repository = AuthRepository(),
                scope = scope
            )
        }

        SignupScreen(
            onSignUpClick = {},
            onSignupSuccess = {},
            onBackToLoginClick = {},
            viewModel = viewModel
        )
    }
}


// --- HOME ---
@Preview(name = "Home", showBackground = true, showSystemUi = true)
@Composable
fun HomePreview() {
    AppTheme {
        HomeScreen()
    }
}

@Preview(name = "Profile", showBackground = true, showSystemUi = true)
@Composable
fun ProfilePreview() {
    AppTheme {
        ProfileScreen(
            patient = TODO(),
            onBackClick = TODO()
        )
    }
}