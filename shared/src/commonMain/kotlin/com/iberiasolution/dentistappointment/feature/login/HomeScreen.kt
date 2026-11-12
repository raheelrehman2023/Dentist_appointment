package com.iberiasolution.dentistappointment.feature.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iberiasolution.dentistappointment.themes.AppColors
import com.iberiasolution.dentistappointment.themes.Dimens


// ============ 1. DATA MODELS ============
// In a real app these come from API / Database.
// We put them here so you learn the structure first.

data class Dentist(
    val id: Int,
    val name: String,
    val specialty: String,
    val rating: String,
    val experience: String,
    val color: Color
)

data class DentalService(
    val id: Int,
    val name: String,
    val price: String,
    val duration: String,
    val icon: String
)

// Fake data - like a backend would return
private val dentists = listOf(
    Dentist(1, "Dr. Maria Santos", "Orthodontist", "4.9", "8 yrs", AppColors.Primary),
    Dentist(2, "Dr. João Ferreira", "Implantologist", "4.8", "12 yrs", AppColors.Secondary),
    Dentist(3, "Dr. Ana Costa", "Pediatric Dentist", "5.0", "6 yrs", AppColors.Success),
)

private val services = listOf(
    DentalService(1, "Cleaning", "€45", "30 min", "✦"),
    DentalService(2, "Whitening", "€120", "60 min", "✧"),
    DentalService(3, "Braces", "€1,500", "Consult", "◈"),
    DentalService(4, "Implant", "€900", "90 min", "⬢"),
    DentalService(5, "Check-up", "€25", "20 min", "●"),
)

private val days = listOf("Mon 10", "Tue 11", "Wed 12", "Thu 13", "Fri 14", "Sat 15")
private val times = listOf("09:00", "10:00", "11:00", "14:00", "15:00", "16:00", "17:00")

// ============ 2. HOME SCREEN ============
// This is the MAIN screen. It holds all the STATE:
// selectedDentist, selectedService, selectedDay, selectedTime
// In Compose, UI = f(state). When state changes, UI re-draws automatically.

@Composable
fun HomeScreen(
    userName: String = "João",
    onDentistClick: (Int) -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onBookAppointment: (String) -> Unit = {}
) {
    // --- STATE ---
    // remember = survives recomposition (screen redraw)
    // mutableStateOf = when value changes, Compose redraws UI
    var selectedDentist by remember { mutableStateOf<Dentist?>(dentists[0]) }
    var selectedService by remember { mutableStateOf<DentalService?>(null) }
    var selectedDay by remember { mutableStateOf<String?>(null) }
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableStateOf(0) }
    var showConfirmation by remember { mutableStateOf(false) }

    // Derived state: can we book? Only if all 4 are chosen
    val canBook = selectedDentist != null && selectedService != null &&
            selectedDay != null && selectedTime != null

    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // LazyColumn = scrollable list, only renders visible items (performant)
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = Dimens.Large),
            verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
        ) {
            // --- HEADER ---
            item {
                Spacer(Modifier.height(Dimens.Medium))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            "Good Morning, $userName",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                        Text(
                            "Healthy Smile Starts Here",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    // Avatar
                    Box(
                        Modifier.size(48.dp)
                            .background(Brush.linearGradient(listOf(AppColors.Primary, AppColors.Secondary)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("J", color = AppColors.SurfaceLight, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // --- NEXT APPOINTMENT CARD ---
            // This is a static card for learning. Later it comes from DB.
            item {
                Card(
                    shape = RoundedCornerShape(Dimens.RadiusLarge),
                    colors = CardDefaults.cardColors(containerColor = AppColors.Primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(Dimens.Medium)) {
                        Text("NEXT APPOINTMENT", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.8f))
                        Spacer(Modifier.height(Dimens.Small))
                        Text("Cleaning with Dr. Maria Santos", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(Dimens.ExtraSmall))
                        Text("Tomorrow • 10:00 • Clínica Castelo Branco", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.9f))
                        Spacer(Modifier.height(Dimens.Medium))
                        Row {
                            Box(
                                Modifier.background(Color.White, RoundedCornerShape(Dimens.RadiusFull))
                                    .padding(horizontal = Dimens.Medium, vertical = Dimens.Small)
                            ) {
                                Text("Reschedule", color = AppColors.Primary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            }
                            Spacer(Modifier.width(Dimens.Small))
                            Box(
                                Modifier.background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(Dimens.RadiusFull))
                                    .padding(horizontal = Dimens.Medium, vertical = Dimens.Small)
                            ) {
                                Text("Cancel", color = Color.White, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }

            // --- STEP 1: CHOOSE DENTIST ---
            item { SectionTitle("1. Choose Dentist") }
            items(dentists) { dentist ->
                DentistCard(
                    dentist = dentist,
                    isSelected = selectedDentist?.id == dentist.id,
                    onClick = {
                        selectedDentist = dentist
                        onDentistClick(dentist.id)
                    }
                )
            }

            // --- STEP 2: CHOOSE SERVICE ---
            item { SectionTitle("2. Choose Service") }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(Dimens.Small)) {
                    items(services) { service ->
                        val selected = selectedService?.id == service.id
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .width(92.dp)
                                .background(
                                    if (selected) AppColors.Primary else MaterialTheme.colorScheme.surface,
                                    RoundedCornerShape(Dimens.RadiusMedium)
                                )
                                .clickable { selectedService = service }
                                .padding(Dimens.Medium)
                        ) {
                            Text(service.icon, style = MaterialTheme.typography.headlineMedium, color = if (selected) Color.White else AppColors.Primary)
                            Spacer(Modifier.height(Dimens.ExtraSmall))
                            Text(service.name, style = MaterialTheme.typography.labelSmall, color = if (selected) Color.White else MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                            Text(service.price, style = MaterialTheme.typography.labelSmall, color = if (selected) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                            Text(service.duration, style = MaterialTheme.typography.labelSmall, color = if (selected) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                        }
                    }
                }
            }

            // --- STEP 3: CHOOSE DATE ---
            item { SectionTitle("3. Choose Date") }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(Dimens.Small)) {
                    items(days) { day ->
                        val selected = selectedDay == day
                        Box(
                            Modifier
                                .background(if (selected) AppColors.Primary else MaterialTheme.colorScheme.surface, RoundedCornerShape(Dimens.RadiusMedium))
                                .clickable { selectedDay = day }
                                .padding(horizontal = Dimens.Medium, vertical = Dimens.Small),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(day, color = if (selected) Color.White else MaterialTheme.colorScheme.onBackground, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }

            // --- STEP 4: CHOOSE TIME ---
            item { SectionTitle("4. Choose Time") }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(Dimens.Small)) {
                    items(times) { time ->
                        val selected = selectedTime == time
                        Box(
                            Modifier
                                .background(if (selected) AppColors.Primary else MaterialTheme.colorScheme.surface, RoundedCornerShape(Dimens.RadiusMedium))
                                .clickable { selectedTime = time }
                                .padding(horizontal = Dimens.Medium, vertical = Dimens.Small)
                        ) {
                            Text(time, color = if (selected) Color.White else MaterialTheme.colorScheme.onBackground, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }

            // --- CONFIRMATION MESSAGE ---
            if (showConfirmation) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = AppColors.Success.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(Dimens.RadiusMedium),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "✓ Booked! ${selectedService?.name} with ${selectedDentist?.name} on $selectedDay at $selectedTime",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(Dimens.Medium)
                        )
                    }
                }
            }

            // --- BOOK BUTTON ---
            item {
                Button(
                    onClick = {
                        if (canBook) {
                            showConfirmation = true
                            onBookAppointment("${selectedService?.name} - $selectedDay $selectedTime")
                        }
                    },
                    enabled = canBook, // Button disabled until all steps done - good UX
                    shape = RoundedCornerShape(Dimens.RadiusMedium),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary),
                    modifier = Modifier.fillMaxWidth().height(Dimens.ButtonHeight)
                ) {
                    Text(
                        if (canBook) "Book Appointment • ${selectedService?.price}" else "Select Dentist, Service, Date & Time",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
                Spacer(Modifier.height(Dimens.Medium))
            }
        }

        // --- BOTTOM NAV ---
        Row(
            Modifier.fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = Dimens.Small),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomItem("Home", "⌂", selectedTab == 0) { selectedTab = 0 }
            BottomItem("Appointments", "◷", selectedTab == 1) { selectedTab = 1 }
            BottomItem("Dentists", "◉", selectedTab == 2) { selectedTab = 2 }
            BottomItem("Profile", "○", selectedTab == 3) {
                selectedTab = 3
                onProfileClick()
            }
        }
    }
}

// ============ 3. SMALL REUSABLE COMPONENTS ============
// We extract these so HomeScreen stays readable.
// This is called "Composition" - building big UI from small pieces.

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
}

@Composable
private fun DentistCard(dentist: Dentist, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(Dimens.RadiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) AppColors.Primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) Dimens.ElevationMedium else Dimens.ElevationSmall),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(Modifier.padding(Dimens.Medium), verticalAlignment = Alignment.CenterVertically) {
            // Avatar with initial
            Box(
                Modifier.size(56.dp).background(dentist.color.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(dentist.name.removePrefix("Dr. ").first().toString(), style = MaterialTheme.typography.headlineMedium, color = dentist.color, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(Dimens.Medium))
            Column(Modifier.weight(1f)) {
                Text(dentist.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(dentist.specialty, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                Text("★ ${dentist.rating} • ${dentist.experience} exp.", style = MaterialTheme.typography.labelSmall, color = AppColors.Accent)
            }
            // Radio indicator - shows selected state
            Box(
                Modifier.size(24.dp)
                    .background(if (isSelected) AppColors.Primary else Color.Transparent, CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) Text("✓", color = Color.White, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun BottomItem(label: String, icon: String, selected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clip(RoundedCornerShape(Dimens.RadiusMedium))
            .clickable { onClick() }
            .padding(horizontal = Dimens.Medium, vertical = Dimens.ExtraSmall)
    ) {
        Text(icon, style = MaterialTheme.typography.headlineMedium, color = if (selected) AppColors.Primary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
        Text(label, style = MaterialTheme.typography.labelSmall, color = if (selected) AppColors.Primary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
    }
}