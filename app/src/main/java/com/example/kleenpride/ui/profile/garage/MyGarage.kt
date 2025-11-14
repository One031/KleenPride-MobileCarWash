package com.example.kleenpride.ui.profile.garage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.horizontalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kleenpride.R
import com.example.kleenpride.ui.components.BottomNavBar
import com.example.kleenpride.ui.components.CustomButton
import com.example.kleenpride.ui.components.CustomTextField
import com.example.kleenpride.ui.theme.LimeGreen
import com.example.kleenpride.viewmodel.Vehicle
import com.example.kleenpride.viewmodel.VehicleViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyGarageScreen(
   viewModel: VehicleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val vehicles by viewModel.vehicles.observeAsState(emptyList())
    val error by viewModel.error.observeAsState()
    var editVehicle by remember { mutableStateOf<Vehicle?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    // load from firestore
    LaunchedEffect(Unit) {
        viewModel.loadVehicles()
    }

    Surface(
        color = Color.Black,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .padding(bottom = 80.dp) // space for bottom nav
            ) {
                // Header with vehicle icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DirectionsCar,
                        contentDescription = "Garage Icon",
                        tint = LimeGreen,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "My Garage",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(
                    thickness = 2.dp,
                    color = LimeGreen.copy(alpha = 0.4f),
                    modifier = Modifier.background(horizontalGradient(listOf(LimeGreen, Color(0xFFB2FF59))))
                )
                Spacer(modifier = Modifier.height(30.dp))

                // Vehicle list
                vehicles.forEach { vehicle ->
                    var expanded by remember { mutableStateOf(false) }
                    val cardHeight by animateDpAsState(if (expanded) 320.dp else 120.dp)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .height(cardHeight)
                            .clickable { expanded = !expanded }
                            .shadow(8.dp, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(listOf(Color(0xFF0E0E0E), Color(0xFF1A1A1A))),
                                    RoundedCornerShape(16.dp)
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .background(
                                            horizontalGradient(listOf(LimeGreen, Color(0xFFB2FF59))),
                                            RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                                        )
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Vehicle title row
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        val vehicleIcon = when (vehicle.type.lowercase()) {
                                            "suv" -> R.drawable.suv2
                                            "sedan" -> R.drawable.sedan2
                                            "hatchback" -> R.drawable.hatch2
                                            else -> null
                                        }

                                        if (vehicleIcon != null) {
                                            Box(
                                                modifier = Modifier
                                                    .size(60.dp)
                                                    .padding(end = 10.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Image(
                                                    painter = painterResource(id = vehicleIcon),
                                                    contentDescription = vehicle.type,
                                                    modifier = Modifier.fillMaxHeight().aspectRatio(1f),
                                                    contentScale = ContentScale.Fit
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                        }

                                        Column {
                                            Text(
                                                text = vehicle.make,
                                                color = Color.White,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = vehicle.type,
                                                color = Color.Gray,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }

                                    Text(
                                        text = if (expanded) "▲" else "▼",
                                        color = LimeGreen,
                                        fontSize = 20.sp
                                    )
                                }

                                AnimatedVisibility(visible = expanded) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        HorizontalDivider(
                                            thickness = 1.dp,
                                            color = LimeGreen.copy(alpha = 0.4f)
                                        )

                                        VehicleInfoRow(label = "Type", value = vehicle.type)
                                        VehicleInfoRow(label = "Make", value = vehicle.make)

                                        Spacer(modifier = Modifier.height(8.dp))

                                        CustomButton(
                                            text = "Edit Vehicle",
                                            onClick = {
                                                editVehicle = vehicle.copy()
                                                coroutineScope.launch { sheetState.show() }
                                            },
                                            containerColor = LimeGreen,
                                            contentColor = Color.Black,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        editVehicle = Vehicle("", "", "")
                        coroutineScope.launch { sheetState.show() }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = horizontalGradient(listOf(LimeGreen, Color(0xFFB2FF59))),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Add Vehicle",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

            }

            // bottom nav bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                BottomNavBar(currentScreen = "garage")
            }
        }
    }

    // Bottom sheet for Add/Edit vehicle
    if (editVehicle != null) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch { sheetState.hide() }
                editVehicle = null
            },
            sheetState = sheetState,
            containerColor = Color(0xFF101010),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            EditVehicleSheetContent(
                vehicle = editVehicle!!,
                onDismiss = {
                    coroutineScope.launch { sheetState.hide() }
                    editVehicle = null
                },
                onDelete = {
                    // call viewModel to delete
                    editVehicle?.let { viewModel.deleteVehicle(it) };
                    coroutineScope.launch { sheetState.hide() }
                    editVehicle = null
                },
                onSave = { newVehicle ->
                    // save via ViewModel (add or update)
                    viewModel.saveVehicle(newVehicle)
                    coroutineScope.launch { sheetState.hide() }
                    editVehicle = null
                }
            )
        }
    }

    error?.let {
        LaunchedEffect(it) {
            coroutineScope.launch {
                sheetState.hide()
            }
        }
    }
}

@Composable
fun VehicleInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 15.sp, fontWeight = FontWeight.Medium)
        Text(text = value, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditVehicleSheetContent(
    vehicle: Vehicle,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onSave: (Vehicle) -> Unit
) {
    var type by remember { mutableStateOf(vehicle.type) }
    var make by remember { mutableStateOf(vehicle.make) }
    val vehicleTypes = listOf("SUV", "Sedan", "Hatchback")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = if (vehicle.make.isEmpty()) "Add Vehicle" else "Edit Vehicle",
            color = LimeGreen,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            TextField(
                value = type,
                onValueChange = {},
                readOnly = true,
                label = { Text("Type", color = Color.White) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .clickable { expanded = true },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedContainerColor = Color.Black,
                    unfocusedContainerColor = Color.Black,
                    focusedIndicatorColor = LimeGreen,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedLabelColor = LimeGreen,
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = LimeGreen,
                    focusedTextColor = LimeGreen,
                    unfocusedTextColor = LimeGreen
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.Black)
            ) {
                vehicleTypes.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, color = LimeGreen) },
                        onClick = {
                            type = option
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        CustomTextField(value = make, onValueChange = { make = it }, label = "Make")
        Spacer(modifier = Modifier.height(12.dp))
        Spacer(modifier = Modifier.height(24.dp))

        CustomButton(
            text = "Save",
            onClick = {
                vehicle.type = type
                vehicle.make = make
                onSave(vehicle)
            },
            containerColor = LimeGreen,
            contentColor = Color.Black,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        CustomButton(
            text = "Delete",
            onClick = onDelete,
            containerColor = Color.Red,
            contentColor = Color.White,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        CustomButton(
            text = "Cancel",
            onClick = onDismiss,
            containerColor = Color.Gray,
            contentColor = Color.White,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyGarageScreenPreview() {
    MyGarageScreen()
}
