package com.example.kleenpride.ui.booking

import android.R.color.black
import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kleenpride.data.models.BookingState
import com.example.kleenpride.data.models.CarType
import com.example.kleenpride.data.models.PaymentMethod
import com.example.kleenpride.data.models.Service
import com.example.kleenpride.ui.components.BottomNavBar
import com.example.kleenpride.ui.components.CarTypeDisplay
import com.example.kleenpride.ui.components.OptionCard
import com.example.kleenpride.ui.components.PaymentDisplay
import com.example.kleenpride.ui.components.SectionHeader
import com.example.kleenpride.ui.components.SelectionCard
import com.example.kleenpride.ui.components.ServiceDisplay
import com.example.kleenpride.ui.components.TimeField
import com.example.kleenpride.ui.components.TimeSlotGrid
import com.example.kleenpride.ui.components.loadPaymentMethodsFromFirestore
import com.example.kleenpride.ui.components.textFieldColors
import com.example.kleenpride.ui.theme.LimeGreen
import java.text.SimpleDateFormat
import java.util.*

// Color Constants
@SuppressLint("ResourceAsColor")
private val BackgroundBlack = Color(black)
private val CardBlack = Color(0xFF1A1A1A)
private val TextWhite = Color(0xFFFFFFFF)
private val TextGray = Color(0xFFB0B0B0)
private val BorderGray = Color(0xFF2A2A2A)

/**
 * Main booking screen - users select service, date/time, location, and vehicle type
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewBookingScreen(onBookingConfirmed: (BookingState) -> Unit = {}) {

    var bookingState by remember { mutableStateOf(BookingState()) }
    var expandedSection by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Load payment methods from Firestore
    var paymentMethods by remember { mutableStateOf<List<PaymentMethod>>(emptyList()) }
    var isLoadingPayments by remember { mutableStateOf(true) }

    // Load payment methods when screen opens
    LaunchedEffect(Unit) {
        loadPaymentMethodsFromFirestore { methods ->
            paymentMethods = methods
            isLoadingPayments = false
        }
    }

    // Get current time in milliseconds
    val currentTimeMillis = System.currentTimeMillis()

    // Create date picker state that only allows future dates
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentTimeMillis,
        initialDisplayedMonthMillis = null,
        // Restricting the year range
        yearRange = IntRange(2024, 2030),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // Only allow dates from today onwards
                return utcTimeMillis >= currentTimeMillis
            }

            override fun isSelectableYear(year: Int): Boolean {
                // Only allow current year and future years
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                return year >= currentYear
            }
        }
    )


    val services = remember { loadServices() }
    val carTypes = remember { loadCarTypes() }
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Spacer(modifier = Modifier.height(26.dp))
            // Header
            Text(
                text = "New Booking",
                style = MaterialTheme.typography.titleLarge,
                color = LimeGreen,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Service Selection
            SectionHeader("Select Service")
            SelectionCard(
                selectedItem = bookingState.selectedService,
                isExpanded = expandedSection == "service",
                onToggle = {
                    expandedSection = if (expandedSection == "service") null else "service"
                },
                placeholder = "Tap to select service"
            ) {
                ServiceDisplay(it)
            }

            if (expandedSection == "service") {
                services.forEach { service ->
                    OptionCard(
                        isSelected = bookingState.selectedService?.id == service.id,
                        onClick = {
                            bookingState = bookingState.copy(selectedService = service)
                            expandedSection = null
                        }
                    ) {
                        ServiceDisplay(service)
                    }
                }
            }

            // Date & Time Section
            SectionHeader("Date & Time")
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Date Card
                Column(modifier = Modifier.weight(1f)) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker = true },
                        colors = CardDefaults.cardColors(containerColor = CardBlack),
                        border = BorderStroke(1.dp, BorderGray)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = bookingState.selectedDate?.let {
                                    SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(it)
                                } ?: "Select date",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (bookingState.selectedDate != null) TextWhite else TextGray,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(12.dp)) // Space between text and icon
                            Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = "Select date",
                                tint = LimeGreen
                            )
                        }
                    }
                }

                // Time Card
                Column(modifier = Modifier.weight(1f)) {
                    TimeField(
                        selectedTime = bookingState.selectedTime,
                        isExpanded = expandedSection == "time",
                        onToggle = {
                            expandedSection = if (expandedSection == "time") null else "time"
                        }
                    )
                }
            }

            if (expandedSection == "time") {
                TimeSlotGrid(
                    timeSlots = timeSlots,
                    selectedTime = bookingState.selectedTime,
                    onTimeSelected = {
                        bookingState = bookingState.copy(selectedTime = it)
                        expandedSection = null
                    }
                )
            }

            // Location
            SectionHeader("Location")
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = bookingState.address,
                onValueChange = { bookingState = bookingState.copy(address = it) },
                placeholder = { Text("Enter your address", color = TextGray) },
                trailingIcon = {
                    Icon(Icons.Default.LocationOn, null, tint = LimeGreen)
                },
                singleLine = true,
                colors = textFieldColors()
            )

            SectionHeader("Car Type")
            SelectionCard(
                selectedItem = bookingState.selectedCarType,
                isExpanded = expandedSection == "carType",
                onToggle = { expandedSection = if (expandedSection == "carType") null else "carType" },
                placeholder = "Tap to select car type"
            ) {
                CarTypeDisplay(it)
            }

            if (expandedSection == "carType") {
                carTypes.forEach { carType ->
                    OptionCard(
                        isSelected = bookingState.selectedCarType?.id == carType.id,
                        onClick = {
                            bookingState = bookingState.copy(selectedCarType = carType)
                            expandedSection = null
                        }
                    ) {
                        CarTypeDisplay(carType)
                    }
                }
            }

            // Payment Method
            SectionHeader("Payment Method")

            if (isLoadingPayments) {
                // Show loading indicator
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CardBlack),
                    border = BorderStroke(1.dp, BorderGray)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(color = LimeGreen, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Loading payment methods...", color = TextGray)
                    }
                }
            } else if (paymentMethods.isEmpty()) {
                // No payment methods available
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CardBlack),
                    border = BorderStroke(1.dp, BorderGray)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No payment methods found", color = TextGray)
                        Text("Please add a payment method in your profile",
                            color = TextGray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            } else {
                // Show payment method selection
                SelectionCard(
                    selectedItem = bookingState.selectedPaymentMethod,
                    isExpanded = expandedSection == "payment",
                    onToggle = { expandedSection = if (expandedSection == "payment") null else "payment" },
                    placeholder = "Tap to select payment method"
                ) {
                    PaymentDisplay(it)
                }

                if (expandedSection == "payment") {
                    paymentMethods.forEach { payment ->
                        OptionCard(
                            isSelected = bookingState.selectedPaymentMethod?.id == payment.id,
                            onClick = {
                                bookingState = bookingState.copy(selectedPaymentMethod = payment)
                                expandedSection = null
                            }
                        ) {
                            PaymentDisplay(payment)
                        }
                    }
                }
            }

            // Confirm Button
            Button(
                modifier = Modifier.fillMaxWidth().height(56.dp),
                onClick = { onBookingConfirmed(bookingState) },
                enabled = bookingState.isValid(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LimeGreen,
                    contentColor = Black,
                    disabledContainerColor = BorderGray
                )
            ) {
                Text(
                    "Confirm Booking",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        BottomNavBar(currentScreen = "booking")
    }

    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        bookingState = bookingState.copy(selectedDate = Date(millis))
                    }
                    showDatePicker = false
                }) {
                    Text("Confirm", color = LimeGreen)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = LimeGreen)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = LimeGreen,
                    todayContentColor = LimeGreen
                )
            )
        }
    }
}

// Data loaders
private fun loadServices() =  listOf(
    Service(1, "Pride Wash", "30 min", "R350", "Basic wash and vacuum"),
    Service(2, "Premium Detail", "45 min", "R1200", "Wash, wax, and detailing"),
    Service(3, "Deluxe Clean", "60 min", "R750", "Full deep cleaning"),
    Service(4, "Executive Package", "90 min", "R950", "Complete detailing")
)

private fun loadCarTypes() =  listOf(
    CarType(1, "Sedan"),
    CarType(2, "SUV", "🚙"),
    CarType(3, "Hatchback", "🚐")
)

private fun loadPaymentMethods() = listOf(
    PaymentMethod("1", "Visa", "4242", "💳"),
    PaymentMethod("2", "Mastercard", "5678", "💳"),
    PaymentMethod("3", "Cash", "", "💵")
)

private fun generateTimeSlots() = (8..17).flatMap { hour ->
    listOf(0, 30).mapNotNull { minute ->
        if (hour == 17 && minute == 30) null else {
            val displayHour = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
            val period = if (hour < 12 ) "AM" else "PM"
            String.format(Locale.getDefault(), "%d:%02d %s", displayHour, minute, period)
        }
    }
}