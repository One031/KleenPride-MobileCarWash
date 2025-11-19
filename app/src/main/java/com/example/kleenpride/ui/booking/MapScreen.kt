package com.example.kleenpride.ui.booking

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.*

@SuppressLint("MissingPermission")
@Composable
fun MapScreen() {

    val context = LocalContext.current

    // Location provider
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Driver location (your phone running the app)
    var driverLocation by remember { mutableStateOf<LatLng?>(null) }

    // Load last known location
    LaunchedEffect(true) {
        fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
            if (loc != null) {
                driverLocation = LatLng(loc.latitude, loc.longitude)
            } else {
                // fallback to Cape Town if location disabled
                driverLocation = LatLng(-33.918861, 18.423300)
            }
        }
    }

    // Camera state centers on driver until customer added
    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            driverLocation ?: LatLng(-33.918861, 18.423300),
            14f
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = "Your Location",
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            cameraPositionState = cameraState
        ) {
            driverLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "You"
                )
            }
        }
    }
}
