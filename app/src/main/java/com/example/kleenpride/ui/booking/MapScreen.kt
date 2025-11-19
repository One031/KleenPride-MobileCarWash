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

    // CUSTOMER LOCATION (fixed example, later from booking)
    val customerLocation = LatLng(-33.918861, 18.423300)   // Cape Town

    // Location provider
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Load last known driver location
    LaunchedEffect(true) {
        fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
            if (loc != null) {
                driverLocation = LatLng(loc.latitude, loc.longitude)
            }
        }
    }

    // CAMERA centers on CUSTOMER
    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            customerLocation,   // center of map
            14f
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = "Map Overview",
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // MAP UI
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
        }
    }
}
