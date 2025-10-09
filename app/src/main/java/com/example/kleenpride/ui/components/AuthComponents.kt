package com.example.kleenpride.ui.components


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalTextStyle // for isError
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false, //readonly for certain text fields, eg. dropdowns
    isError: Boolean = false, // error indicator for fields left out
    textStyle: TextStyle = LocalTextStyle.current.copy(color = Color.White)
) {
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = if (isError) Color.Red else Color.Green, //  red border on focus if error
        unfocusedBorderColor = if (isError) Color.Red else Color.White, // white normally. This just changes the state when the field is left out
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.Green,
        cursorColor = Color.White,
        focusedLeadingIconColor = Color.White,
        unfocusedLeadingIconColor = Color.White,
        focusedLabelColor = Color.Gray,
        unfocusedLabelColor = Color.Gray
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = modifier.fillMaxWidth(),
        readOnly = readOnly,
        colors = textFieldColors,
        textStyle = textStyle
    )
}

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier.fillMaxWidth(0.7f),
        shape = RoundedCornerShape(30.dp)
    ) {
        Text(text = text)
    }
}

@Composable
fun OrSeparator(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(0.7f).padding(vertical = 16.dp)
    ) {
        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.weight(1f))
        Text(text = "OR", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.weight(1f))
    }
}

