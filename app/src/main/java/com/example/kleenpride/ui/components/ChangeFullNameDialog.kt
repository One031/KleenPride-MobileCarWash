package com.example.kleenpride.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kleenpride.ui.theme.LimeGreen

@Composable
fun ChangeFullNameDialog(
    currentFullName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var newFullName by remember { mutableStateOf(currentFullName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Full Name", color = Color.White, fontWeight = FontWeight.Bold) },
        text  = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Current: $currentFullName", color = Color.Gray, fontSize = 14.sp)

                OutlinedTextField(
                    value = newFullName,
                    onValueChange = { newFullName = it },
                    label = { Text("Full Name") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LimeGreen,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = LimeGreen,
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = LimeGreen
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(newFullName) },
                enabled = newFullName.isNotBlank() && newFullName != currentFullName
            ) {
                Text("Update", color = LimeGreen)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        },
        containerColor = Color(0xFF1A1A1A)
        )
}