package com.example.todolist

import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import org.osmdroid.api.IMapController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Overlay
@Composable
fun MapScreen(navController: NavController, onLocationSelected: (Pair<Double, Double>) -> Unit) {
    var selectedLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    // Integrating OSMDroid Map View using AndroidView
    AndroidView(
        factory = { context ->
            val mapView = MapView(context)
            mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
            mapView.setMultiTouchControls(true)

            // Center the map on a default location
            val mapController: IMapController = mapView.controller
            mapController.setCenter(GeoPoint(44.319305, 23.8006)) // Default: Bucharest
            mapController.setZoom(15.0)

            // Add click listener for selecting location
            mapView.overlays.add(object : Overlay() {
                override fun onSingleTapConfirmed(e: MotionEvent, mapView: MapView): Boolean {
                    val geoPoint = mapView.projection.fromPixels(e.x.toInt(), e.y.toInt()) as GeoPoint
                    selectedLocation = Pair(geoPoint.latitude, geoPoint.longitude)

                    // Add a marker at the selected location
                    val marker = Marker(mapView).apply {
                        position = geoPoint
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        title = "Selected Location"
                    }
                    mapView.overlays.clear()
                    mapView.overlays.add(marker)
                    mapView.invalidate() // Refresh map

                    Toast.makeText(context, "Location: ${geoPoint.latitude}, ${geoPoint.longitude}", Toast.LENGTH_SHORT).show()
                    return true
                }
            })

            mapView
        },
        modifier = Modifier.fillMaxSize()
    )

    // If a location is selected, return it
    selectedLocation?.let {
        Button(
            onClick = {
                onLocationSelected(it)
                navController.popBackStack("new_list", false)
            },
            modifier = Modifier
                .wrapContentWidth()  // Ensure it doesn’t stretch
                .wrapContentHeight()
                .padding(16.dp)
        ) {
            Text("Confirm Location")
        }
    }
}