package com.example.kleenpride.ui.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kleenpride.ui.components.BottomNavBar
import java.text.SimpleDateFormat
import java.util.*

/**
 * Shown to the user to create new booking.
 * The user is prompted to fill in information such as service, Date & Time, And Location before creating the booking.
 */

// Color Constants
private val BackgroundBlack = Color(0xFF0A0A0A)
private val CardBlack = Color(0xFF1A1A1A)
private val PrimaryGreen = Color(0xFF00FF88)
private val SecondaryGreen = Color(0xFF00CC6A)
private val TextWhite = Color(0xFFFFFFFF)
private val TextGray = Color(0xFFB0B0B0)
private val BorderGray = Color(0xFF2A2A2A)

// Data class for Service
data class Service(
    val id: Int,
    val name: String,
    val duration: String,
    val price: String,
    val description: String = ""
)

// Data class for Car Type
data class CarType(
    val id: Int,
    val name: String,
    val icon: String = "🚗",
    val description: String = ""
)

@Composable
fun CreateNewBookingScreen(
    onBookingConfirmed: () -> Unit = {},
    onNavigateTo: (String) -> Unit = {}
) {
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var address by remember { mutableStateOf("") }
    var selectedCarType by remember { mutableStateOf<CarType?>(null) }
    var selectedService by remember { mutableStateOf<Service?>(null) }
    var showServiceSelection by remember { mutableStateOf(false) }
    var showCarTypeSelection by remember { mutableStateOf(false) }
    var showTimeSelection by remember { mutableStateOf(false) }

    // Define available services
    val services = listOf(
        Service(
            id = 1,
            name = "Pride Wash",
            duration = "30 min",
            price = "R350",
            description = "Basic exterior wash and interior vacuum"
        ),
        Service(
            id = 2,
            name = "Premium Detail",
            duration = "45 min",
            price = "R1200",
            description = "Exterior wash, wax, interior detailing, and tire shine"
        ),
        Service(
            id = 3,
            name = "Deluxe Clean",
            duration = "60 min",
            price = "R750",
            description = "Full exterior and interior deep cleaning with leather treatment"
        ),
        Service(
            id = 4,
            name = "Executive Package",
            duration = "90 min",
            price = "R950",
            description = "Complete detailing including engine bay cleaning and paint protection"
        )
    )

    // Define available car types to select from
    val carTypes = listOf(
        CarType(
            id = 1,
            name = "Sedan",
            icon = "🚗",
            description = "Standard 4-door car"
        ),
        CarType(
            id = 2,
            name = "SUV",
            icon = "🚙",
            description = "Sports Utility Vehicle"
        ),
        CarType(
            id = 3,
            name = "Hatchback",
            icon = "🚐",
            description = "Compact 3 or 5-door car"
        )
    )

    // Generate time slots (8:00 AM to 6:00 PM in 30-minute intervals)
    val timeSlots = remember {
        generateTimeSlots()
    }

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

            // Service Selection Section
            ServiceSelectionSection(
                services = services,
                selectedService = selectedService,
                onServiceSelected = { service ->
                    selectedService = service
                    showServiceSelection = false
                },
                showServiceSelection = showServiceSelection,
                onToggleServiceSelection = { showServiceSelection = !showServiceSelection }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Date & Time Section
            DateTimeSection(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                selectedTime = selectedTime,
                onTimeSelected = { time ->
                    selectedTime = time
                    showTimeSelection = false
                },
                showTimeSelection = showTimeSelection,
                onToggleTimeSelection = { showTimeSelection = !showTimeSelection },
                timeSlots = timeSlots
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Location Section
            LocationSection(
                address = address,
                onAddressChanged = { address = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Car Type Selection Section
            CarTypeSelectionSection(
                carTypes = carTypes,
                selectedCarType = selectedCarType,
                onCarTypeSelected = { carType ->
                    selectedCarType = carType
                    showCarTypeSelection = false
                },
                showCarTypeSelection = showCarTypeSelection,
                onToggleCarTypeSelection = { showCarTypeSelection = !showCarTypeSelection }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Confirm Booking Button
            ConfirmBookingButton(
                enabled = isFormValid(selectedService, selectedDate, selectedTime, address, selectedCarType),
                onConfirm = {
                    // Handle booking confirmation with selected service and car type
                    val bookingDetails = BookingDetails(
                        service = selectedService!!,
                        carType = selectedCarType!!,
                        date = selectedDate,
                        time = selectedTime,
                        address = address
                    )
                    onBookingConfirmed()
                }
            )
        }

        // Bottom Navigation Bar at the bottom
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
            color = PrimaryGreen,
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
private fun DateTimeSection(
    selectedDate: Date?,
    onDateSelected: (Date) -> Unit,
    selectedTime: String?,
    onTimeSelected: (String) -> Unit,
    showTimeSelection: Boolean,
    onToggleTimeSelection: () -> Unit,
    timeSlots: List<String>
) {
    Column {
        Text(
            text = "Date & Time",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Date Picker
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Date",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = selectedDate?.let {
                        SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(it)
                    } ?: "yyyy/mm/dd",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = "Select date",
                            tint = PrimaryGreen
                        )
                    },
                    placeholder = {
                        Text("Select date", color = TextGray)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        unfocusedBorderColor = BorderGray,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        cursorColor = PrimaryGreen,
                        focusedContainerColor = CardBlack,
                        unfocusedContainerColor = CardBlack,
                        disabledContainerColor = CardBlack
                    )
                )
            }

            // Time Picker
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Time Selection Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleTimeSelection() },
                    colors = CardDefaults.cardColors(
                        containerColor = CardBlack
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = if (showTimeSelection) {
                        BorderStroke(2.dp, PrimaryGreen)
                    } else {
                        BorderStroke(1.dp, BorderGray)
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedTime ?: "Select time",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (selectedTime != null) TextWhite else TextGray
                        )
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Select time",
                            tint = PrimaryGreen
                        )
                    }
                }

                // Time Options (shown when expanded)
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
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = CardBlack
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, BorderGray)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Available Time Slots",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Create a grid with 3 columns
            val chunkedTimeSlots = timeSlots.chunked(3)

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                chunkedTimeSlots.forEach { rowTimes ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowTimes.forEach { time ->
                            TimeSlotItem(
                                time = time,
                                isSelected = selectedTime == time,
                                onTimeSelected = onTimeSelected,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Fill remaining slots if row has less than 3 items
                        if (rowTimes.size < 3) {
                            repeat(3 - rowTimes.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
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
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp),
        border = if (isSelected) {
            BorderStroke(2.dp, PrimaryGreen)
        } else {
            BorderStroke(1.dp, BorderGray)
        }
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
private fun ServiceSelectionSection(
    services: List<Service>,
    selectedService: Service?,
    onServiceSelected: (Service) -> Unit,
    showServiceSelection: Boolean,
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

        // Selected Service Display
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleServiceSelection() },
            colors = CardDefaults.cardColors(
                containerColor = CardBlack
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = if (showServiceSelection) {
                BorderStroke(2.dp, PrimaryGreen)
            } else {
                BorderStroke(1.dp, BorderGray)
            }
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                if (selectedService != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
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
                            if (selectedService.description.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = selectedService.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextGray
                                )
                            }
                        }

                        Text(
                            text = selectedService.price,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tap to select a service",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextGray
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select service",
                            tint = PrimaryGreen
                        )
                    }
                }
            }
        }

        // Service Options (shown when expanded)
        if (showServiceSelection) {
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                services.forEach { service ->
                    ServiceOptionItem(
                        service = service,
                        isSelected = selectedService?.id == service.id,
                        onServiceSelected = onServiceSelected
                    )
                }
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
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                CardBlack.copy(alpha = 0.8f)
            } else {
                CardBlack
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = if (isSelected) {
            BorderStroke(2.dp, PrimaryGreen)
        } else {
            BorderStroke(1.dp, BorderGray)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = service.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = service.duration,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGray
                )
                if (service.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = service.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray
                    )
                }
            }

            Text(
                text = service.price,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        }
    }
}

@Composable
private fun CarTypeSelectionSection(
    carTypes: List<CarType>,
    selectedCarType: CarType?,
    onCarTypeSelected: (CarType) -> Unit,
    showCarTypeSelection: Boolean,
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

        // Selected Car Type Display
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleCarTypeSelection() },
            colors = CardDefaults.cardColors(
                containerColor = CardBlack
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = if (showCarTypeSelection) {
                BorderStroke(2.dp, PrimaryGreen)
            } else {
                BorderStroke(1.dp, BorderGray)
            }
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                if (selectedCarType != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedCarType.icon,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Column {
                                Text(
                                    text = selectedCarType.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                if (selectedCarType.description.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = selectedCarType.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextGray
                                    )
                                }
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select car type",
                            tint = PrimaryGreen
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tap to select car type",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextGray
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select car type",
                            tint = PrimaryGreen
                        )
                    }
                }
            }
        }

        // Car Type Options (shown when expanded)
        if (showCarTypeSelection) {
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                carTypes.forEach { carType ->
                    CarTypeOptionItem(
                        carType = carType,
                        isSelected = selectedCarType?.id == carType.id,
                        onCarTypeSelected = onCarTypeSelected
                    )
                }
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
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                CardBlack.copy(alpha = 0.8f)
            } else {
                CardBlack
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = if (isSelected) {
            BorderStroke(2.dp, PrimaryGreen)
        } else {
            BorderStroke(1.dp, BorderGray)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = carType.icon,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(end = 16.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = carType.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                if (carType.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = carType.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray
                    )
                }
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Selected",
                    tint = PrimaryGreen
                )
            }
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

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = address,
            onValueChange = onAddressChanged,
            placeholder = {
                Text("Enter your address", color = TextGray)
            },
            trailingIcon = {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = PrimaryGreen
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = PrimaryGreen,
                unfocusedIndicatorColor = BorderGray,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                cursorColor = PrimaryGreen,
                focusedContainerColor = CardBlack,
                unfocusedContainerColor = CardBlack,
                disabledContainerColor = CardBlack
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
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryGreen,
            contentColor = BackgroundBlack,
            disabledContainerColor = BorderGray,
            disabledContentColor = TextGray
        )
    ) {
        Text(
            text = "Confirm Booking",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun generateTimeSlots(): List<String> {
    val timeSlots = mutableListOf<String>()
    val startHour = 8 // 8 AM
    val endHour = 18 // 6 PM

    for (hour in startHour..endHour) {
        for (minute in listOf(0, 30)) {
            if (hour == endHour && minute == 30) break // Stop at 6:00 PM

            val period = if (hour < 12) "AM" else "PM"
            val displayHour = when {
                hour == 0 -> 12
                hour > 12 -> hour - 12
                else -> hour
            }
            val timeString = String.format(
                "%d:%02d %s",
                displayHour,
                minute,
                period
            )
            timeSlots.add(timeString)
        }
    }
    return timeSlots
}

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

data class BookingDetails(
    val service: Service,
    val carType: CarType,
    val date: Date?,
    val time: String?,
    val address: String
)

@Preview(showBackground = true)
@Composable
fun CreateNewBookingScreenPreview() {
    MaterialTheme {
        CreateNewBookingScreen()
    }
}