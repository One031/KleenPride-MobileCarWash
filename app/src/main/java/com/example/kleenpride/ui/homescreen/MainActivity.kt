package com.example.kleenpride.ui.homescreen

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.kleenpride.R
import com.example.kleenpride.ui.theme.LimeGreen
import com.example.kleenpride.viewmodel.LocationViewModel
import com.example.kleenpride.viewmodel.UserDataViewModel
import com.example.kleenpride.viewmodel.VehicleViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.BLACK

        setContent {
            HomeScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: UserDataViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
               vehicleViewModel: VehicleViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
               locationViewModel: LocationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val today = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault()).format(Date())

    var selectedService by remember { mutableStateOf<ServiceItem?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val userData by viewModel.userData.observeAsState()
    val vehicles by vehicleViewModel.vehicles.observeAsState(emptyList())
    val locations by locationViewModel.locations.observeAsState(emptyList())
    val error by viewModel.error.observeAsState()

    LaunchedEffect(Unit) {
        vehicleViewModel.loadVehicles()
        locationViewModel.loadLocations()
    }

    // Find the default vehicle using the userData.defaultVehicleId
    val defaultVehicle = vehicles.firstOrNull { it.id == userData?.defaultVehicleId } ?: vehicles.firstOrNull()

    val defaultLocation = locations.firstOrNull {it.id == userData?.defaultAddressId} ?: locations.firstOrNull()

    val carBrand = defaultVehicle?.make ?: "Loading..."
    val carSize = defaultVehicle?.type ?: "Loading..."
    val address = defaultLocation?.address ?: "Loading..."

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {

        // --- Top Header ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Location",
                        tint = LimeGreen
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Home", color = Color.White, fontWeight = FontWeight.Bold)
                            Icon(
                                imageVector = Icons.Filled.ExpandMore,
                                contentDescription = "Dropdown",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            address,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                carBrand,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Filled.ExpandMore,
                                contentDescription = "Dropdown",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            carSize,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.DirectionsCar,
                        contentDescription = "Car",
                        tint = LimeGreen
                    )
                }
            }
        }

        // --- Banner ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.car_banner),
                contentDescription = "Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Pride in Every Ride",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Get Quick Wash",
                    color = Color.White,
                    fontSize = 18.sp
                )
                Button(
                    onClick = { Toast.makeText(context, "Book Now clicked", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier.padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LimeGreen)
                ) {
                    Text("Book Now", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }

        // --- Our Services ---
        Text(
            text = "Our Services",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Choose from our wide range of professional services",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        val services = listOf(
            ServiceItem("Pride Wash", R.drawable.pridewash, "R110 - R140", "30 min", "Quick exterior wash to keep your car looking fresh",
                listOf("Full Exterior Was", "Interior Clean", "Clean Windows", "Floor Mats Vacuum", "Seat Vacuum", "Tyre Shine", "Full Car Trim", "Car Perfume")),
            ServiceItem("Wash & Go", R.drawable.washgo, "R80 - R100", "20 min", "A fast and simple wash for when you’re on the move",
                listOf("Full Exterior Wash", "Clean Windows", "Tyre shine", "Exterior Trim")),
            ServiceItem("Interior Detailing", R.drawable.cardetailing, "R50 - R65", "45 min", "Deep interior clean for a spotless finish",
                listOf("Full House Vacuum", "Clean Panels", "Dash Board Treatment", "Car Perfume")),
            ServiceItem("Car Valet & Detailing", R.drawable.carvalet, "R450", "60 min", "Complete inside and out detailing service",
                listOf("Pre Wash", "Full Exterior Wash", "Clean Windows", "Deep Vacuum", "Seats Vacuum & Deep Cleaned", "Floor Mats Vacuum & Deep Cleaned", "Detail Brushing", "Tyre Shine", "Trimming", "Car Perfume"))
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(services) { service ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            selectedService = service
                            showDialog = true
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.DarkGray)
                            .border(2.dp, LimeGreen, RoundedCornerShape(16.dp))
                    ) {
                        Image(
                            painter = painterResource(id = service.image),
                            contentDescription = service.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(service.name, fontSize = 14.sp, color = Color.White, textAlign = TextAlign.Center)
                }
            }
        }

        // --- Popular Services ---
        Text(
            text = "Popular Services",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .clickable {
                    Toast.makeText(context, "Quick Wash clicked", Toast.LENGTH_SHORT).show()
                }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("CAR VALET & DETAILING", fontWeight = FontWeight.Bold, color = Color.White)
                    Text("From R450", color = Color.Gray, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        com.example.kleenpride.ui.components.BottomNavBar(currentScreen = "home")
    }

    if (showDialog && selectedService != null) {
        ServiceDetailsDialog(
            service = selectedService!!,
            onDismiss = { showDialog = false },
            onBookClick = {
                showDialog = false
                Toast.makeText(context, "Booking ${selectedService!!.name}", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

// --- Models ---
data class ServiceItem(
    val name: String,
    val image: Int,
    val price: String,
    val duration: String,
    val description: String,
    val included: List<String>
)

// --- Popup Dialog ---
@Composable
fun ServiceDetailsDialog(service: ServiceItem, onDismiss: () -> Unit, onBookClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        containerColor = Color(0xFF1B1B1B),
        shape = RoundedCornerShape(16.dp),
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    // Show the actual service image
                    Image(
                        painter = painterResource(id = service.image),
                        contentDescription = service.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Overlay for the price tag and car icon
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f))
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DirectionsCar,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.25f), RoundedCornerShape(50))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = service.price,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(service.name, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.ExpandMore, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Text(service.duration, color = Color.Gray, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(service.description, color = Color.Gray, fontSize = 14.sp, textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(12.dp))
                Text("What's Included:", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    service.included.forEach { item ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_check_circle),
                                contentDescription = null,
                                tint = LimeGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(item, color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onBookClick,
                    colors = ButtonDefaults.buttonColors(containerColor = LimeGreen),
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                ) {
                    Text("Book This Service", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {}
    )
}

@Preview(showSystemUi = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}