package com.example.kleenpride.ui.profile.location

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.horizontalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kleenpride.ui.components.CustomButton
import com.example.kleenpride.ui.components.CustomTextField
import com.example.kleenpride.ui.theme.LimeGreen
import kotlinx.coroutines.launch
import com.example.kleenpride.ui.components.BottomNavBar

data class Location(
    var name: String,
    var address: String,
    var comment: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLocationsScreen(
    locations: MutableList<Location> = mutableListOf(
        Location("Home", "123 Maple Street", "Main entrance gate"),
        Location("Work", "45 Commerce Ave", "3rd level parking")
    ),
) {
    var editLocation by remember { mutableStateOf<Location?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            BottomNavBar(currentScreen = "Locations")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Location Icon",
                    tint = LimeGreen,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "My Locations",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(
                thickness = 2.dp,
                color = LimeGreen.copy(alpha = 0.4f),
                modifier = Modifier.background(
                    horizontalGradient(listOf(LimeGreen, Color(0xFFB2FF59)))
                )
            )
            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                locations.forEach { location ->
                    var expanded by remember { mutableStateOf(false) }
                    val cardHeight by animateDpAsState(if (expanded) 150.dp else 100.dp)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .height(cardHeight)
                            .shadow(8.dp, RoundedCornerShape(20.dp))
                            .clickable { expanded = !expanded },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF0E0E0E), Color(0xFF1A1A1A))
                                    ),
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
                                            horizontalGradient(listOf(LimeGreen, Color(0xFFB2FF59))
                                            ),
                                            RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                                        )
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column {
                                        Text(
                                            text = location.address,
                                            color = Color.White,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = location.name,
                                            color = Color.Gray,
                                            fontSize = 14.sp
                                        )
                                    }

                                    // Options menu
                                    var showMenu by remember { mutableStateOf(false) }
                                    Box {
                                        IconButton(onClick = { showMenu = true }) {
                                            Icon(
                                                painter = androidx.compose.ui.res.painterResource(
                                                    android.R.drawable.ic_menu_more
                                                ),
                                                contentDescription = "More options",
                                                tint = LimeGreen
                                            )
                                        }
                                        DropdownMenu(
                                            expanded = showMenu,
                                            onDismissRequest = { showMenu = false },
                                            modifier = Modifier.background(Color.Black)
                                        ) {
                                            DropdownMenuItem(
                                                text = { Text("Edit", color = LimeGreen) },
                                                onClick = {
                                                    editLocation = location
                                                    coroutineScope.launch { sheetState.show() }
                                                    showMenu = false
                                                }
                                            )
                                            DropdownMenuItem(
                                                text = { Text("Delete", color = Color.Red) },
                                                onClick = {
                                                    locations.remove(location)
                                                    showMenu = false
                                                }
                                            )
                                        }
                                    }
                                }

                                AnimatedVisibility(visible = expanded) {
                                    Column(modifier = Modifier.padding(top = 10.dp)) {
                                        HorizontalDivider(
                                            thickness = 1.dp,
                                            color = LimeGreen.copy(alpha = 0.4f)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = location.comment.ifEmpty { "No comments added for this location." },
                                            color = Color.Gray,
                                            fontSize = 14.sp
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
                        editLocation = Location("", "", "")
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
                                brush = horizontalGradient(
                                    listOf(LimeGreen, Color(0xFFB2FF59))
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Add Location",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }

    // Bottom sheet for add/edit
    if (editLocation != null) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch { sheetState.hide() }
                editLocation = null
            },
            sheetState = sheetState,
            containerColor = Color(0xFF101010),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            EditLocationSheet(
                location = editLocation!!,
                onDismiss = {
                    coroutineScope.launch { sheetState.hide() }
                    editLocation = null
                },
                onDelete = {
                    locations.remove(editLocation)
                    coroutineScope.launch { sheetState.hide() }
                    editLocation = null
                },
                onSave = { newLocation ->
                    if (!locations.contains(newLocation)) locations.add(newLocation)
                    coroutineScope.launch { sheetState.hide() }
                    editLocation = null
                }
            )
        }
    }
}

@Composable
fun EditLocationSheet(
    location: Location,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onSave: (Location) -> Unit
) {
    var name by remember { mutableStateOf(location.name) }
    var address by remember { mutableStateOf(location.address) }
    var comment by remember { mutableStateOf(location.comment) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = if (location.name.isEmpty()) "Add Location" else "Edit Location",
            color = LimeGreen,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        CustomTextField(value = name, onValueChange = { name = it }, label = "Location Name")
        Spacer(modifier = Modifier.height(12.dp))
        CustomTextField(value = address, onValueChange = { address = it }, label = "Address")
        Spacer(modifier = Modifier.height(12.dp))
        CustomTextField(
            value = comment,
            onValueChange = { comment = it },
            label = "Comment (optional)"
        )
        Spacer(modifier = Modifier.height(24.dp))

        CustomButton(
            text = "Save",
            onClick = {
                location.name = name
                location.address = address
                location.comment = comment
                onSave(location)
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
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyLocationsScreenPreview() {
    MyLocationsScreen(
        locations = mutableListOf(
            Location("Home", "123 Maple Street", "Use side gate"),
            Location("Work", "45 Commerce Ave", "3rd level parking"),
            Location("Parents", "678 Oak Lane", "Ring bell twice")
        )
    )
}
