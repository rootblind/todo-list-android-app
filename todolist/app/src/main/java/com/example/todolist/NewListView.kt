package com.example.todolist

import android.R.attr.onClick
import android.graphics.Paint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost


@Composable
fun NewListView(navController: NavController) {
    var inputName by remember {
        mutableStateOf("")
    }
    var inputDesc by remember {
        mutableStateOf("")
    }

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    LaunchedEffect(savedStateHandle) {
        val newLocation = savedStateHandle?.get<Pair<Double, Double>>("selectedLocation")
        if (newLocation != null) {
            location = newLocation
            savedStateHandle.remove<Pair<Double, Double>>("selectedLocation") // Clear after use
        }
    }


    Column(
        modifier = Modifier.fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ){
            Text("New list", style = MaterialTheme.typography.titleLarge)
        }

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Name",
                modifier = Modifier.align(Alignment.CenterVertically)
                    .width(60.dp)
                )
            OutlinedTextField(
                modifier = Modifier.weight(1f)
                    .weight(1f),
                value = inputName,
                onValueChange = {
                    inputName = it
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Description",
                modifier = Modifier.align(Alignment.CenterVertically)
                    .width(95.dp)
            )
            OutlinedTextField(
                modifier = Modifier.weight(1f)
                    .weight(1f),
                value = inputDesc,
                onValueChange = {
                    inputDesc = it
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text = "Location",
                modifier = Modifier.align(Alignment.CenterVertically)
                    .width(95.dp)
            )

            Button(
                onClick = {
                    navController.navigate("open_map")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_location_24),
                    contentDescription = "Location",
                    tint = Color.White
                )
            }
        }

        location?.let {
            Text("Selected Location: Lat: ${it.first}, Lng: ${it.second}")
        }
    }

}