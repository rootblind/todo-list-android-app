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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun TodoListEditView(navController: NavController, viewModel: TodoViewModel, id: Int) {
    val todo by produceState<Todo?>(initialValue = null) {
        viewModel.viewModelScope.launch {
            value = viewModel.todoDao.getTodoById(id)
        }
    }

    todo?.let {
        var inputName by rememberSaveable {
            mutableStateOf(todo!!.name)
        }
        var inputDesc by rememberSaveable {
            mutableStateOf(todo!!.description)
        }

        var ctx = LocalContext.current

        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
        var location by remember { mutableStateOf(
            Pair(todo!!.longitude, todo!!.latitude)
        ) }

        var addressName by remember{ mutableStateOf(todo?.address) }

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
                Text("Edit list", style = MaterialTheme.typography.titleLarge)
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
                    value = inputName.toString(),
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
                    value = inputDesc.toString(),
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
                    colors = ButtonDefaults.buttonColors(colorScheme.primary)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_location_24),
                        contentDescription = "Location",
                        tint = Color.White
                    )
                }
            }

            location.let {
                val context = LocalContext.current
                val address = getAddressFromCoordinates(context, location.first, location.second)
                addressName = address
                Text("Location: $addressName")
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
                .padding((16.dp))

        ) {
            FloatingActionButton(
                onClick = {
                    if(inputName.isEmpty() || inputDesc.isEmpty()) {
                        Toast.makeText(ctx, "All fields must be completed.", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.viewModelScope.launch {
                            viewModel.update(
                                Todo(
                                id = todo!!.id,
                                name = inputName,
                                description = inputDesc,
                                longitude = location.first,
                                latitude = location.second,
                                address = addressName.toString()
                                )
                            )
                            navController.popBackStack()
                        }


                    }
                },
                containerColor = Color.Blue,
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Add",
                    tint = Color.White
                )
            }
        }
    }

}