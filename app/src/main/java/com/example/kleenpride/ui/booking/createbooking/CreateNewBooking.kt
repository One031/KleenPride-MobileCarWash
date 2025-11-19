package com.example.kleenpride.ui.booking.createbooking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
private val BackgroundBlack = Color(0xFF000000)
private val CardBlack = Color(0xFF1A1A1A)
private val TextWhite = Color(0xFFFFFFFF)
private val TextGray = Color(0xFFB0B0B0)
private val BorderGray = Color(0xFF2A2A2A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewBookingScreen(
    preselectedServiceName: String = "",
    preselectedServicePrice: String = "",
    preselectedServiceDuration: String = "",
    preselectedLocation: String = "",
    preselectedCarType: String = "",
    onBookingConfirmed: (BookingState) -> Unit = {}
) {
    val services = remember { loadServices() }
    val carTypes = remember { loadCarTypes() }
    val timeSlots = remember { generateTimeSlots() }

    // Find and set preselected service if provided
    val initialService = remember(preselectedServiceName) {
        if (preselectedServiceName.isNotEmpty()) {
            services.find { it.name == preselectedServiceName }
        } else {
            null
        }
    }

    // Find preselected car type
    val initialCarType = remember(preselectedCarType) {
        if (preselectedCarType.isNotEmpty()) {
            carTypes.find { it.name == preselectedCarType }
        } else {
            null
        }
    }

    // Initialize booking state with ALL preselected values
    var bookingState by remember {
        mutableStateOf(
            BookingState(
                selectedService = initialService,
                selectedCarType = initialCarType,
                address = preselectedLocation
            )
        )
    }

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

    // Update booking state when preselected values change (safety check)
    LaunchedEffect(initialService, initialCarType, preselectedLocation) {
        bookingState = bookingState.copy(
            selectedService = initialService ?: bookingState.selectedService,
            selectedCarType = initialCarType ?: bookingState.selectedCarType,
            address = preselectedLocation.ifEmpty { bookingState.address }
        )
    }

    // Get current time in milliseconds
    val currentTimeMillis = System.currentTimeMillis()

    // Create date picker state that only allows future dates
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentTimeMillis,
        initialDisplayedMonthMillis = null,
        yearRange = IntRange(2024, 2030),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= currentTimeMillis
            }

            override fun isSelectableYear(year: Int): Boolean {
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                return year >= currentYear
            }
        }
    )

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
                        isSelected = bookingState.selectedService?.name == service.name,
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
                            Spacer(modifier = Modifier.width(12.dp))
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
                        isSelected = bookingState.selectedCarType?.name == carType.name,
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
                    contentColor = Color.Black,
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
    Service(1, "Pride Wash", "30 min", "R110", "Quick exterior wash to keep your car looking fresh"),
    Service(2, "Wash & Go", "20 min", "R80", "A fast and simple wash for when you're on the move"),
    Service(3, "Interior Detailing", "45 min", "R55", "Deep interior clean for a spotless finish"),
    Service(4, "Car Valet & Detailing", "60 min", "R450", "Complete inside and out detailing service")
)

private fun loadCarTypes() =  listOf(
    CarType(1, "Sedan"),
    CarType(2, "SUV", "🚙"),
    CarType(3, "Hatchback", "🚐")
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