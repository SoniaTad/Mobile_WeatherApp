package com.example.weatherapp

import LocationStore
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.style.TextAlign
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.Dispatchers


import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Settings : ComponentActivity() {
    private val userStore by lazy { UserStore(this) }
    private val locationStore by lazy { LocationStore(this) }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Greeting(userStore,locationStore)


                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(userStore: UserStore,locationStore:LocationStore) {
    val latitude = remember { mutableDoubleStateOf(0.0) }
    val longitude = remember { mutableDoubleStateOf(0.0) }
    val radioButtonOptionText = remember { mutableStateOf(RadioButtonOption.LessThanTen)}
        LaunchedEffect(Unit) {
            locationStore.getLongitude.collect { long ->
                // Update the text values
                longitude.doubleValue = long
            }

        }

    val context = LocalContext.current
    val locationPermissionGranted = remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showDialog2 by remember { mutableStateOf(false) }
    var showDialog3 by remember { mutableStateOf(false) }
    val coroutine = rememberCoroutineScope()
    val userNameText = remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                navigationIcon = {
                    IconButton(onClick = { context.startActivity(Intent(context, MainActivity::class.java))}) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Arrow"
                        )
                    }
                },
                title = {
                    Text(
                        text = "Settings",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },


            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .background(color = Color(0xFFB88DD3))
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "Location ",
                            modifier = Modifier.weight(1f)
                                .padding(vertical = 8.dp, horizontal = 14.dp)
                        )

                        TextButton(onClick = {
                            coroutine.launch {
                                locationStore.getLocation.collect { enabled ->
                                    locationPermissionGranted.value = enabled
                                }
                                //here


                            }
                            showDialog = true
                        }) {
                            Text(text = "Manage")
                        }

                        if (showDialog) {
                            AlertDialog(
                                onDismissRequest = { showDialog = false },
                                title = { Text(text = "Location State") },
                                text = {
                                    Column {
                                        if (locationPermissionGranted.value) {
                                            Text(text = "The location is currently enabled")
                                            Text(text = "Would you like to disable it?")

                                        } else {
                                            Text(text = "The location is currently disabled")
                                            Text(text = "Would you like to enable it?")

                                        }
                                    }
                                },
                                confirmButton = {
                                    Button(onClick = { if(locationPermissionGranted.value){
                                         locationPermissionGranted.value=false
                                    }
                                        else{locationPermissionGranted.value=true}
                                        coroutine.launch {
                                            withContext(Dispatchers.IO) {
                                                locationStore.saveLoc(context, enabled = locationPermissionGranted.value)}}
                                        showDialog=false
                                    }) {
                                        Text(text = "Yes")
                                    }
                                },
                                dismissButton ={
                                    Button(onClick = { showDialog = false }) {
                                        Text(text = "No")
                                    }
                                }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "View user preferences",
                            modifier = Modifier.weight(1f)
                                .padding(vertical = 8.dp, horizontal = 14.dp)
                        )

                        TextButton(onClick = {
                            coroutine.launch {
                                userStore.getPref.collect { (userName, radioButtonOption) ->
                                    // Update the text values
                                    userNameText.value = userName
                                    radioButtonOptionText.value = radioButtonOption
                                }
                            }
                            showDialog2 = true
                        }) {
                            Text(text = "View")
                        }

                        if (showDialog2) {
                            AlertDialog(
                                onDismissRequest = { showDialog2 = false },
                                title = { Text(text = "User Preferences ") },
                                text = {
                                    Column {
                                        Text(text = "User Name: ${userNameText.value}")
                                        Text(text = "User Preference: ${radioButtonOptionText.value}")
                                    }
                                },
                                confirmButton = {
                                    Button(onClick = { showDialog2 = false }) {
                                        Text(text = "OK")
                                    }
                                }
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "Edit user preferences ",
                            modifier = Modifier.weight(1f)
                                .padding(vertical = 8.dp, horizontal = 14.dp)
                        )

                        TextButton(onClick = {
                            val intent = Intent(context, LogoPage::class.java)
                            context.startActivity(intent)
                        }) {
                            Text(text = "Edit")
                        }}
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "View location details ",
                            modifier = Modifier.weight(1f)
                                .padding(vertical = 8.dp, horizontal = 14.dp)
                        )

                        TextButton(onClick = {
                            coroutine.launch {
                                locationStore.getLatitude.collect { lat ->
                                    // Update the text values
                                    latitude.doubleValue = lat
                                }

                            }
                            showDialog3 = true
                        }) {
                            Text(text = "View")
                        }

                        if (showDialog3) {
                            AlertDialog(
                                onDismissRequest = { showDialog3 = false },
                                title = { Text(text = "User Preferences ") },
                                text = {
                                    Column {
                                        Text(text = "User Name: ${latitude.doubleValue}")
                                        Text(text = "User Name: ${longitude.doubleValue}")
                                    }
                                },
                                confirmButton = {
                                    Button(onClick = { showDialog3 = false }) {
                                        Text(text = "OK")
                                    }
                                }
                            )
                        }
                    }


                }

            }
        }
    )

    }





