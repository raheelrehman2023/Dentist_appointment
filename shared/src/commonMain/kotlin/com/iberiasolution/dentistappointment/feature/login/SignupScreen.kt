package com.iberiasolution.dentistappointment.feature.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iberiasolution.dentistappointment.data.AuthViewModel
import com.iberiasolution.dentistappointment.data.Patient
import com.iberiasolution.dentistappointment.data.PatientSignupData
import com.iberiasolution.dentistappointment.themes.AppColors
import com.iberiasolution.dentistappointment.themes.Dimens




@Composable
fun SignupScreen(
    viewModel: AuthViewModel,
    onSignUpClick: (PatientSignupData) -> Unit,
    onSignupSuccess: () -> Unit,
    onBackToLoginClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var acceptTerms by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    val isValid = name.isNotBlank() &&
            email.isNotBlank() &&
            email.contains("@") &&
            password.length >= 6 &&
            confirmPassword == password &&
            acceptTerms

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onSignupSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(Dimens.Large)
    ) {
        Spacer(modifier = Modifier.height(Dimens.Medium))

        // Logo
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            AppColors.Primary,
                            AppColors.Secondary
                        )
                    ),
                    shape = CircleShape
                )
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "I",
                style = MaterialTheme.typography.displayLarge,
                color = AppColors.SurfaceLight,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(Dimens.ExtraLarge))

        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimens.Small))

        Text(
            text = "Join Iberia Solution in seconds",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimens.ExtraLarge))

        // Name
        Label("Full Name")

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("João Silva") },
            singleLine = true,
            shape = RoundedCornerShape(Dimens.RadiusMedium),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimens.Medium))

        // Email
        Label("Email")

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("you@example.com") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            shape = RoundedCornerShape(Dimens.RadiusMedium),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimens.Medium))

        // Password
        Label("Password")

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Minimum 6 characters") },
            singleLine = true,
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                TextButton(onClick = {
                    passwordVisible = !passwordVisible
                }) {
                    Text(if (passwordVisible) "Hide" else "Show")
                }
            },
            shape = RoundedCornerShape(Dimens.RadiusMedium),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimens.Medium))

        // Confirm Password
        Label("Confirm Password")

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = { Text("Repeat your password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            shape = RoundedCornerShape(Dimens.RadiusMedium),
            modifier = Modifier.fillMaxWidth()
        )

        if (confirmPassword.isNotEmpty() &&
            confirmPassword != password
        ) {
            Text(
                text = "Passwords do not match",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(modifier = Modifier.height(Dimens.Medium))

        // Terms and Conditions
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = acceptTerms,
                onCheckedChange = { acceptTerms = it }
            )

            Text(
                text = "I agree to Terms & Privacy Policy",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(Dimens.Large))

        // Signup Button
        Button(
            onClick = {
                val signupData = PatientSignupData(
                    name = name.trim(),
                    email = email.trim(),
                    password = password
                )

                onSignUpClick(signupData)
            },
            enabled = isValid && !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    "Create Account",
                    style = MaterialTheme.typography.titleMedium,
                    color = AppColors.SurfaceLight
                )
            }
        }

        // Firebase error
        uiState.error?.let { message ->
            Spacer(modifier = Modifier.height(Dimens.Small))

            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(Dimens.Large))

        // Back to Login
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                "Already have an account? ",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Login",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.Primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    onBackToLoginClick()
                }
            )
        }

        Spacer(modifier = Modifier.height(Dimens.Large))
    }
}

@Composable
private fun Label(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(modifier = Modifier.height(Dimens.ExtraSmall))
}

