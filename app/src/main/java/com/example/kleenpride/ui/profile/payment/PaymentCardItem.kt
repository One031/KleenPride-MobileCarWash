package com.example.kleenpride.ui.profile.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kleenpride.ui.theme.LimeGreen

@Composable
fun PaymentCardItem(
    cardAlias: String,
    selected: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 8.dp else 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
           modifier = Modifier
               .background(
                   brush = if (selected)
                   Brush.linearGradient(listOf(Color(0xFF1C1C1C), Color(0xFF363636)))
                    else
                        Brush.linearGradient(listOf(Color(0xFF0E0E0E), Color(0xFF1A1A1A))),
                   RoundedCornerShape(16.dp)
               )
               .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "•••• $cardAlias",
                    color = Color.White,
                    fontSize = 16.sp
                )

                TextButton(onClick = onDelete) {
                    Text("Delete", color = LimeGreen)
                }
            }
        }
    }
}