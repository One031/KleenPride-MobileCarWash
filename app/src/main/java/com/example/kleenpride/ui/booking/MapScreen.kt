package com.example.kleenpride.ui.booking

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@SuppressLint("MissingPermission")
@Composable
fun MapScreen() {

    val context = LocalContext.current

    // Driver location (your device)
    var driverLocation by remember { mutableStateOf<LatLng?>(null) }

    // CUSTOMER LOCATION (fixed for testing)
    val customerLocation = LatLng(-33.918861, 18.423300)

    // Location provider
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Load last known driver location
    LaunchedEffect(true) {
        fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
            if (loc != null) {
                driverLocation = LatLng(loc.latitude, loc.longitude)
            } else {
                driverLocation = LatLng(-33.9200, 18.4200) // fallback for emulator
            }
        }
    }

    // Camera should center on the customer
    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            customerLocation,
            14f
        )
    }

    // DUMMY ETA & DISTANCE
    val eta = "15 min"
    val distance = "12 km"

    // DUMMY ROUTE POINTS (straight line)
    val polylinePoints = remember(driverLocation) {
        if (driverLocation != null) {
            listOf(
                driverLocation!!,
                customerLocation
            )
        } else {
            emptyList()
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = "ETA: $eta   |   Distance: $distance",
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            cameraPositionState = cameraState
        ) {

            // CUSTOMER MARKER
            Marker(
                state = MarkerState(position = customerLocation),
                title = "Customer Location"
            )

            // DRIVER MARKER
            driverLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Your Location"
                )
            }

            // ROUTE LINE (DUMMY)
            if (polylinePoints.isNotEmpty()) {
                Polyline(points = polylinePoints)
            }
        }
    }
}
