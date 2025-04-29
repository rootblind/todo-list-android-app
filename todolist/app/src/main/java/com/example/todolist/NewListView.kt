package com.example.todolist

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun NewListView(navController: NavController, viewModel: TodoViewModel) {
    var inputName by rememberSaveable { mutableStateOf("") }
    var inputDesc by rememberSaveable { mutableStateOf("") }

    var ctx = LocalContext.current

    val colorScheme = MaterialTheme.colorScheme

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var addressName by remember{ mutableStateOf("None") }

    val newTodoId by viewModel.lastId.collectAsState()

    LaunchedEffect(newTodoId) {
        newTodoId?.let {
            id ->
            navController.navigate("todo_page/$id") {
                popUpTo("home") { inclusive = false }
            }
            viewModel.resetLastId()
        }
    }

    LaunchedEffect(savedStateHandle) {
        val newLocation = savedStateHandle?.get<Pair<Double, Double>>("selectedLocation")
        if (newLocation != null) {
            location = newLocation
            savedStateHandle.remove<Pair<Double, Double>>("selectedLocation")
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
            Text("New list", style = typography.titleLarge, color = colorScheme.onBackground)
        }

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Name",
                modifier = Modifier.align(Alignment.CenterVertically)
                    .width(60.dp),
                color = colorScheme.onBackground
                )
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = inputName,
                onValueChange = {
                    inputName = it
                },
                singleLine = true
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
                    .width(95.dp),
                color = colorScheme.onBackground
            )
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = inputDesc,
                onValueChange = {
                    inputDesc = it
                },
                singleLine = true
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
                    .width(95.dp),
                color = colorScheme.onBackground
            )

            Button(
                onClick = {
                    navController.navigate("open_map")
                },
                colors = ButtonDefaults.buttonColors(colorScheme.primary)   //containerColor = Color.Magenta)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_location_24),
                    contentDescription = "Location",
                    tint = colorScheme.onPrimary  //Color.White
                )
            }
        }

        location?.let {
            val context = LocalContext.current
            val address = getAddressFromCoordinates(context, location?.first?: 0.0, location?.second ?: 0.0)
            addressName = address
            Text("Selected Location: $address", color = colorScheme.onBackground)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .padding((16.dp))

    ) {
        FloatingActionButton(
            onClick = {
                if(inputName.isEmpty() || inputDesc.isEmpty() || location == null) {
                    Toast.makeText(ctx, "All fields must be completed.", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.viewModelScope.launch {
                        viewModel.insert(
                            inputName,
                            inputDesc,
                            location!!.first,
                            location!!.second,
                            addressName)
                    }


                }
            },
            containerColor = colorScheme.secondary,
            contentColor = colorScheme.onSecondary,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Add"
            )
        }
    }

}
