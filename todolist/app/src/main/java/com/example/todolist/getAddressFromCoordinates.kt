package com.example.todolist

import androidx.compose.runtime.remember
import android.content.Context
import android.location.Geocoder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import java.util.Locale

@Composable
fun getAddressFromCoordinates(context: Context, latitude: Double, longitude: Double): String {
    var address by remember { mutableStateOf("Fetching address...") }

    LaunchedEffect(latitude, longitude) {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                address = addresses[0].getAddressLine(0) ?: "Address not found"
            } else {
                address = "No address found"
            }
        } catch (e: Exception) {
            address = "Error: ${e.localizedMessage}"
        }
    }

    return address
}
