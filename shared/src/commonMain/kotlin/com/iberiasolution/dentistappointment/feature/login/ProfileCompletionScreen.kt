package com.iberiasolution.dentistappointment.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.iberiasolution.dentistappointment.data.Patient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.rememberCoroutineScope
import dev.icerock.moko.media.compose.BindMediaPickerEffect
import dev.icerock.moko.media.compose.rememberMediaPickerControllerFactory
import dev.icerock.moko.media.compose.toImageBitmap
import dev.icerock.moko.media.picker.MediaSource
import kotlinx.coroutines.launch

@Composable
fun ProfileCompletionScreen(
    name: String,
    email: String,
    isSaving: Boolean = false,
    errorMessage: String? = null,
    onSaveProfile: (Patient) -> Unit,
    saveErrorMessage: String? = null
) {
    var surname by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }

    var isDiabetic by remember { mutableStateOf(false) }
    var hasHighBloodPressure by remember { mutableStateOf(false) }
    var isPregnant by remember { mutableStateOf<Boolean?>(null) }
    var validationError by remember { mutableStateOf<String?>(null) }
    val mediaFactory = rememberMediaPickerControllerFactory()
    val mediaPicker = remember(mediaFactory) {
        mediaFactory.createMediaPickerController()
    }

    val coroutineScope = rememberCoroutineScope()

    var selectedImage by remember {
        mutableStateOf<ImageBitmap?>(null)
    }

    var showImageOptions by remember {
        mutableStateOf(false)
    }

    BindMediaPickerEffect(mediaPicker)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable {
                    showImageOptions = true
                },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImage != null) {
                Image(
                    bitmap = selectedImage!!,
                    contentDescription = "Selected profile photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text("Add Photo")
            }
        }

        Text(
            text = "Tap to add a profile photo",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "Complete Your Profile",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Please provide some additional information "
                    + "to complete your patient profile."
        )

        OutlinedTextField(
            value = name,
            onValueChange = {},
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        OutlinedTextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Surname") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = email,
            onValueChange = {},
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = age,
            onValueChange = { age = it.filter(Char::isDigit) },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        Text(
            text = "Sex",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = sex == "Male",
                onClick = { sex = "Male" }
            )
            Text("Male")

            Spacer(Modifier.width(16.dp))

            RadioButton(
                selected = sex == "Female",
                onClick = { sex = "Female" }
            )
            Text("Female")
        }

        HorizontalDivider()

        Text(
            text = "Medical Information",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isDiabetic,
                onCheckedChange = { isDiabetic = it }
            )
            Text("I have diabetes")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = hasHighBloodPressure,
                onCheckedChange = {
                    hasHighBloodPressure = it
                }
            )
            Text("I have high blood pressure")
        }

        Text(
            text = "Are you pregnant?",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isPregnant == true,
                onClick = { isPregnant = true }
            )
            Text("Yes")

            Spacer(Modifier.width(12.dp))

            RadioButton(
                selected = isPregnant == false,
                onClick = { isPregnant = false }
            )
            Text("No")

            Spacer(Modifier.width(12.dp))

            RadioButton(
                selected = isPregnant == null,
                onClick = { isPregnant = null }
            )
            Text("N/A")
        }

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        validationError?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        saveErrorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                when {
                    surname.isBlank() -> {
                        validationError = "Please enter your surname."
                    }

                    phone.isBlank() -> {
                        validationError = "Please enter your phone number."
                    }

                    age.isBlank() || age.toIntOrNull() == null || age.toInt() <= 0 -> {
                        validationError = "Please enter a valid age."
                    }

                    sex.isBlank() -> {
                        validationError = "Please select your sex."
                    }

                    else -> {
                        val patient = Patient(
                            name = name,
                            surname = surname.trim(),
                            email = email,
                            phone = phone.trim(),
                            age = age.toInt(),
                            sex = sex,
                            isDiabetic = isDiabetic,
                            hasHighBloodPressure = hasHighBloodPressure,
                            isPregnant = isPregnant
                        )

                        onSaveProfile(patient)
                    }
                }
            },
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isSaving) {
                CircularProgressIndicator()
            } else {
                Text("Save Profile")
            }
        }
    }
    if (showImageOptions) {
        AlertDialog(
            onDismissRequest = {
                showImageOptions = false
            },
            title = {
                Text("Choose Profile Photo")
            },
            text = {
                Text("Would you like to take a photo or choose one from your gallery?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showImageOptions = false

                        coroutineScope.launch {
                            try {
                                val result = mediaPicker.pickImage(
                                    MediaSource.CAMERA
                                )
                                selectedImage = result.toImageBitmap()
                            } catch (e: Exception) {
                                validationError = "Unable to capture photo."
                            }
                        }
                    }
                ) {
                    Text("Camera")
                }
            },
            dismissButton = {
                Row {
                    TextButton(
                        onClick = {
                            showImageOptions = false

                            coroutineScope.launch {
                                try {
                                    val result = mediaPicker.pickImage(
                                        MediaSource.GALLERY
                                    )
                                    selectedImage = result.toImageBitmap()
                                } catch (e: Exception) {
                                    validationError = "Unable to select photo."
                                }
                            }
                        }
                    ) {
                        Text("Gallery")
                    }

                    TextButton(
                        onClick = {
                            showImageOptions = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            }
        )
    }
}


