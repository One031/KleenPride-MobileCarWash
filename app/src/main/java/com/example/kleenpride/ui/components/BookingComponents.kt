package com.example.kleenpride.ui.components

import android.R.color.black
import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kleenpride.data.models.CarType
import com.example.kleenpride.data.models.PaymentMethod
import com.example.kleenpride.data.models.Service
import com.example.kleenpride.ui.theme.LimeGreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Color Constants
@SuppressLint("ResourceAsColor")
private val BackgroundBlack = Color(black)
private val CardBlack = Color(0xFF1A1A1A)
private val TextWhite = Color(0xFFFFFFFF)
private val TextGray = Color(0xFFB0B0B0)
private val BorderGray = Color(0xFF2A2A2A)

/**
 * Reusable selection card component
 */

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = TextWhite
    )
}

@Composable
fun <T> SelectionCard(
    selectedItem: T?,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    placeholder: String,
    content: @Composable RowScope.(T) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onToggle() },
        colors = CardDefaults.cardColors(containerColor = CardBlack),
        border = BorderStroke(if (isExpanded) 2.dp else 1.dp, if (isExpanded) LimeGreen else BorderGray)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            if (selectedItem != null) {
                content(selectedItem)
            } else {
                Text(placeholder, style = MaterialTheme.typography.bodyMedium, color = TextGray)
            }
            Icon(Icons.Default.ArrowDropDown, null , tint = LimeGreen)
        }
    }
}

@Composable
fun OptionCard(
    isSelected: Boolean,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = CardBlack),
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) LimeGreen else BorderGray)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@Composable
fun RowScope.ServiceDisplay(service: Service) {
    Column(modifier = Modifier.weight(1f)) {
        Text(service.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextWhite)
        Text(service.duration, style = MaterialTheme.typography.bodyMedium, color = TextGray)
    }
    Text(service.price, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = LimeGreen)
}

@Composable
fun CarTypeDisplay(carType: CarType) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(carType.icon, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(end = 12.dp))
        Text(carType.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextWhite)
    }
}

@Composable
fun PaymentDisplay(payment: PaymentMethod) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            payment.icon,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(end = 12.dp)
        )
        Column {
            Text(
                payment.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            if (payment.lastDigits.isNotEmpty()) {
                Text(
                    "•••• ${payment.lastDigits}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGray
                )
            }
        }
    }
}

@Composable
fun TimeField(selectedTime: String?, isExpanded: Boolean, onToggle: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onToggle() },
        colors = CardDefaults.cardColors(containerColor = CardBlack),
        border = BorderStroke(
            if (isExpanded) 2.dp else 1.dp,
            if (isExpanded) LimeGreen else BorderGray
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedTime ?: "Select time",
                style = MaterialTheme.typography.bodyMedium,
                color = if (selectedTime != null) TextWhite else TextGray,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp)) // Added space between text and icon
            Icon(Icons.Default.Schedule, contentDescription = "Select time", tint = LimeGreen)
        }
    }
}

@Composable
fun TimeSlotGrid(timeSlots: List<String>, selectedTime: String?, onTimeSelected: (String) -> Unit) {

    Card(colors = CardDefaults.cardColors(containerColor = CardBlack), border = BorderStroke(1.dp,
        BorderGray
    )) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Available Times", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = LimeGreen)
            Spacer(modifier = Modifier.height(12.dp))

            timeSlots.chunked(3).forEach { rowTimes ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    rowTimes.forEach { time ->
                        TimeSlot(time, selectedTime == time, onTimeSelected, Modifier.weight(1f))
                    }
                    repeat(3 - rowTimes.size) {Spacer(Modifier.weight(1f))}
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TimeSlot(time: String, isSelected: Boolean, onSelected: (String) -> Unit, modifier: Modifier) {
    Card(
        modifier = modifier.aspectRatio(1.5f).clickable { onSelected(time) },
        colors = CardDefaults.cardColors(containerColor = if (isSelected) LimeGreen else CardBlack)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(time, style = MaterialTheme.typography.bodyMedium, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) BackgroundBlack else TextWhite)
        }
    }
}

fun loadPaymentMethodsFromFirestore(onLoaded: (List<PaymentMethod>) -> Unit) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    if (userId == null) {
        // User not logged in, return empty list with cash option
        onLoaded(listOf(PaymentMethod("cash", "Cash", "", "💵")))
        return
    }

    val firestore = FirebaseFirestore.getInstance()

    firestore.collection("users")
        .document(userId)
        .collection("paymentMethods")
        .get()
        .addOnSuccessListener { snapshot ->
            val methods = mutableListOf<PaymentMethod>()

            snapshot.documents.forEach { doc ->
                val alias = doc.getString("cardAlias") ?: ""
                val lastDigits = doc.getString("cardNumber")?.takeLast(4) ?: ""
                val token = doc.getString("token") ?: doc.id
                val brand = doc.getString("brand") ?: "Card"

                // Determine icon based on brand
                val icon = when (brand.lowercase()) {
                    "visa" -> "💳"
                    "mastercard" -> "💳"
                    "amex", "american express" -> "💳"
                    else -> "💳"
                }

                methods.add(
                    PaymentMethod(
                        id = token,
                        name = if (alias.isNotEmpty()) alias else brand,
                        lastDigits = lastDigits,
                        icon = icon
                    )
                )
            }

            // Always add cash as an option
            methods.add(PaymentMethod("cash", "Cash", "", "💵"))

            onLoaded(methods)
        }
        .addOnFailureListener { e ->
            // On error, return cash option only
            onLoaded(listOf(PaymentMethod("cash", "Cash", "", "💵")))
        }
}

@Composable
fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = LimeGreen,
    unfocusedBorderColor = BorderGray,
    focusedTextColor = TextWhite,
    unfocusedTextColor = TextWhite,
    cursorColor = LimeGreen,
    focusedContainerColor = CardBlack,
    unfocusedContainerColor = CardBlack
)