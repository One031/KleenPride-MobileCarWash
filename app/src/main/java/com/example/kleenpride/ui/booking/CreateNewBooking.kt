package com.example.kleenpride.ui.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kleenpride.ui.components.BottomNavBar
import com.example.kleenpride.ui.theme.LimeGreen
import java.text.SimpleDateFormat
import java.util.*

// Color Constants
private val BackgroundBlack = Color(0xFF0A0A0A)
private val CardBlack = Color(0xFF1A1A1A)
private val PrimaryGreen = LimeGreen
private val TextWhite = Color(0xFFFFFFFF)
private val TextGray = Color(0xFFB0B0B0)
private val BorderGray = Color(0xFF2A2A2A)

/**
 * Data class representing a car wash service
 */
data class Service(
    val id: Int,
    val name: String,
    val duration: String,
    val price: String,
    val description: String = ""
)

/**
 * Data class representing vehicle type
 */
data class CarType(
    val id: Int,
    val name: String,
    val icon: String = "🚗"
)

/**
 * Main booking screen - users select service, date/time, location, and vehicle type
 */
@Composable
fun CreateNewBookingScreen(onBookingConfirmed: () -> Unit = {}) {
    // State management
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var address by remember { mutableStateOf("") }
    var selectedCarType by remember { mutableStateOf<CarType?>(null) }
    var selectedService by remember { mutableStateOf<Service?>(null) }
    var showServiceSelection by remember { mutableStateOf(false) }
    var showCarTypeSelection by remember { mutableStateOf(false) }
    var showTimeSelection by remember { mutableStateOf(false) }

    // Available options
    val services = remember {
        listOf(
            Service(1, "Pride Wash", "30 min", "R350", "Basic wash and vacuum"),
            Service(2, "Premium Detail", "45 min", "R1200", "Wash, wax, and detailing"),
            Service(3, "Deluxe Clean", "60 min", "R750", "Full deep cleaning"),
            Service(4, "Executive Package", "90 min", "R950", "Complete detailing")
        )
    }

    val carTypes = remember {
        listOf(
            CarType(1, "Sedan"),
            CarType(2, "SUV", "🚙"),
            CarType(3, "Hatchback", "🚐")
        )
    }

    val timeSlots = remember { generateTimeSlots() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlack)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header
            HeaderSection()

            Spacer(modifier = Modifier.height(24.dp))

            // Service Selection
            ServiceSelectionSection(
                services = services,
                selectedService = selectedService,
                showServiceSelection = showServiceSelection,
                onServiceSelected = { service ->
                    selectedService = service
                    showServiceSelection = false
                },
                onToggleServiceSelection = { showServiceSelection = !showServiceSelection }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Date & Time Section - FIXED: Removed unused onDateSelected parameter
            DateTimeSection(
                selectedDate = selectedDate,
                selectedTime = selectedTime,
                showTimeSelection = showTimeSelection,
                timeSlots = timeSlots,
                onTimeSelected = { time ->
                    selectedTime = time
                    showTimeSelection = false
                },
                onToggleTimeSelection = { showTimeSelection = !showTimeSelection }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Location Section
            LocationSection(
                address = address,
                onAddressChanged = { address = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Car Type Selection
            CarTypeSelectionSection(
                carTypes = carTypes,
                selectedCarType = selectedCarType,
                showCarTypeSelection = showCarTypeSelection,
                onCarTypeSelected = { carType ->
                    selectedCarType = carType
                    showCarTypeSelection = false
                },
                onToggleCarTypeSelection = { showCarTypeSelection = !showCarTypeSelection }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Confirm Booking Button
            ConfirmBookingButton(
                enabled = isFormValid(selectedService, selectedDate, selectedTime, address, selectedCarType),
                onConfirm = onBookingConfirmed
            )
        }

        BottomNavBar(currentScreen = "booking")
    }
}

@Composable
private fun HeaderSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "New Booking",
            style = MaterialTheme.typography.headlineMedium,
            color = LimeGreen,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Complete your booking details",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray
        )
    }
}

@Composable
private fun ServiceSelectionSection(
    services: List<Service>,
    selectedService: Service?,
    showServiceSelection: Boolean,
    onServiceSelected: (Service) -> Unit,
    onToggleServiceSelection: () -> Unit
) {
    Column {
        Text(
            text = "Select Service",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Service Selection Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleServiceSelection() },
            colors = CardDefaults.cardColors(containerColor = CardBlack),
            border = BorderStroke(if (showServiceSelection) 2.dp else 1.dp, if (showServiceSelection) PrimaryGreen else BorderGray)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedService != null) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = selectedService.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = selectedService.duration,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextGray
                        )
                    }
                    Text(
                        text = selectedService.price,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
                    )
                } else {
                    Text(
                        text = "Tap to select service",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGray
                    )
                }
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select service", tint = PrimaryGreen)
            }
        }

        // Service Options
        if (showServiceSelection) {
            Spacer(modifier = Modifier.height(12.dp))
            services.forEach { service ->
                ServiceOptionItem(
                    service = service,
                    isSelected = selectedService?.id == service.id,
                    onServiceSelected = onServiceSelected
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ServiceOptionItem(
    service: Service,
    isSelected: Boolean,
    onServiceSelected: (Service) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onServiceSelected(service) },
        colors = CardDefaults.cardColors(containerColor = CardBlack),
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) PrimaryGreen else BorderGray)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = service.name, style = MaterialTheme.typography.titleMedium, color = TextWhite)
                Text(text = service.duration, style = MaterialTheme.typography.bodyMedium, color = TextGray)
            }
            Text(text = service.price, style = MaterialTheme.typography.titleMedium, color = PrimaryGreen)
        }
    }
}

@Composable
private fun DateTimeSection(
    selectedDate: Date?, // FIXED: Removed unused onDateSelected parameter
    selectedTime: String?,
    showTimeSelection: Boolean,
    timeSlots: List<String>,
    onTimeSelected: (String) -> Unit,
    onToggleTimeSelection: () -> Unit
) {
    Column {
        Text(
            text = "Date & Time",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Date Field
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Date", style = MaterialTheme.typography.bodyMedium, color = TextGray)
                // FIXED: Using proper Material3 OutlinedTextField
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = selectedDate?.let {
                        // FIXED: Added explicit locale to prevent warning
                        SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(it)
                    } ?: "yyyy/mm/dd",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Select date", tint = PrimaryGreen)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        unfocusedBorderColor = BorderGray,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        cursorColor = PrimaryGreen,
                        focusedContainerColor = CardBlack,
                        unfocusedContainerColor = CardBlack
                    )
                )
            }

            // Time Field
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Time", style = MaterialTheme.typography.bodyMedium, color = TextGray)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleTimeSelection() },
                    colors = CardDefaults.cardColors(containerColor = CardBlack),
                    border = BorderStroke(if (showTimeSelection) 2.dp else 1.dp, if (showTimeSelection) PrimaryGreen else BorderGray)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedTime ?: "Select time",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (selectedTime != null) TextWhite else TextGray
                        )
                        Icon(Icons.Default.Schedule, contentDescription = "Select time", tint = PrimaryGreen)
                    }
                }

                // Time Selection Grid
                if (showTimeSelection) {
                    Spacer(modifier = Modifier.height(8.dp))
                    TimeSelectionGrid(
                        timeSlots = timeSlots,
                        selectedTime = selectedTime,
                        onTimeSelected = onTimeSelected
                    )
                }
            }
        }
    }
}

@Composable
private fun TimeSelectionGrid(
    timeSlots: List<String>,
    selectedTime: String?,
    onTimeSelected: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBlack),
        border = BorderStroke(1.dp, BorderGray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Available Time Slots",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3-column grid layout
            timeSlots.chunked(3).forEach { rowTimes ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    rowTimes.forEach { time ->
                        TimeSlotItem(
                            time = time,
                            isSelected = selectedTime == time,
                            onTimeSelected = onTimeSelected,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Fill empty spaces
                    repeat(3 - rowTimes.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun TimeSlotItem(
    time: String,
    isSelected: Boolean,
    onTimeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(1.5f)
            .clickable { onTimeSelected(time) },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PrimaryGreen else CardBlack
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) BackgroundBlack else TextWhite
            )
        }
    }
}

@Composable
private fun CarTypeSelectionSection(
    carTypes: List<CarType>,
    selectedCarType: CarType?,
    showCarTypeSelection: Boolean,
    onCarTypeSelected: (CarType) -> Unit,
    onToggleCarTypeSelection: () -> Unit
) {
    Column {
        Text(
            text = "Car Type",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Car Type Selection Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleCarTypeSelection() },
            colors = CardDefaults.cardColors(containerColor = CardBlack),
            border = BorderStroke(if (showCarTypeSelection) 2.dp else 1.dp, if (showCarTypeSelection) PrimaryGreen else BorderGray)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedCarType != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = selectedCarType.icon,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text(
                            text = selectedCarType.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                } else {
                    Text(
                        text = "Tap to select car type",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGray
                    )
                }
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select car type", tint = PrimaryGreen)
            }
        }

        // Car Type Options
        if (showCarTypeSelection) {
            Spacer(modifier = Modifier.height(12.dp))
            carTypes.forEach { carType ->
                CarTypeOptionItem(
                    carType = carType,
                    isSelected = selectedCarType?.id == carType.id,
                    onCarTypeSelected = onCarTypeSelected
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun CarTypeOptionItem(
    carType: CarType,
    isSelected: Boolean,
    onCarTypeSelected: (CarType) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCarTypeSelected(carType) },
        colors = CardDefaults.cardColors(containerColor = CardBlack),
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) PrimaryGreen else BorderGray)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = carType.icon,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(
                text = carType.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }
    }
}

@Composable
private fun LocationSection(
    address: String,
    onAddressChanged: (String) -> Unit
) {
    Column {
        Text(
            text = "Location",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )

        Spacer(modifier = Modifier.height(8.dp))

        // FIXED: Using proper Material3 OutlinedTextField
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = address,
            onValueChange = onAddressChanged,
            placeholder = {
                Text("Enter your address", color = TextGray)
            },
            trailingIcon = {
                Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = PrimaryGreen)
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryGreen,
                unfocusedBorderColor = BorderGray,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                cursorColor = PrimaryGreen,
                focusedContainerColor = CardBlack,
                unfocusedContainerColor = CardBlack
            )
        )
    }
}

@Composable
private fun ConfirmBookingButton(
    enabled: Boolean,
    onConfirm: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        onClick = onConfirm,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryGreen,
            contentColor = BackgroundBlack,
            disabledContainerColor = BorderGray
        )
    ) {
        Text(
            text = "Confirm Booking",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Generates time slots from 8:00 AM to 6:00 PM
 */
private fun generateTimeSlots(): List<String> {
    return (8..17).flatMap { hour ->
        listOf(0, 30).mapNotNull { minute ->
            if (hour == 17 && minute == 30) null else {
                val displayHour = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
                val period = if (hour < 12) "AM" else "PM"
                String.format(Locale.getDefault(), "%d:%02d %s", displayHour, minute, period)
            }
        }
    }
}
/**
 * Validation fields are filled
 */
private fun isFormValid(
    selectedService: Service?,
    selectedDate: Date?,
    selectedTime: String?,
    address: String,
    selectedCarType: CarType?
): Boolean {
    return selectedService != null &&
            selectedDate != null &&
            selectedTime != null &&
            address.isNotBlank() &&
            selectedCarType != null
}
@Preview
@Composable
fun CreateNewBookingScreenPreview() {
    MaterialTheme {
        CreateNewBookingScreen()
    }
}