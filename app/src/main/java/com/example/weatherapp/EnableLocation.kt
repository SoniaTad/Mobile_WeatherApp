package com.example.weatherapp

import LocationStore
import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EnableLocation : ComponentActivity() {
    private val locationStore by lazy { LocationStore(this) }
    private val userStore by lazy { UserStore(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            LocationScreen(locationStore,userStore)
        }
    }
}


@Composable
fun LocationScreen(locationStore:LocationStore,userStore: UserStore) {
    val userNameText = remember { mutableStateOf("") }
    val radioButtonOptionText = remember { mutableStateOf(RadioButtonOption.LessThanTen) }
    LaunchedEffect(Unit) {
        userStore.getPref.collect { (name,choice)->
            userNameText.value = name
            radioButtonOptionText.value=choice
        }
    }


    val context = LocalContext.current

    val locationPermissionGranted = remember { mutableStateOf(false) }


    //val locationPermissionGranted by remember { mutableStateOf(locationPerm.value) }
    val coroutine = rememberCoroutineScope()
    // Create a permission launcher
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted: Boolean ->
                if (isGranted) {
                    // Permission granted, update the location
                    getCurrentLocation(context) { latitude, longitude ->

                        coroutine.launch {
                            withContext(Dispatchers.IO) {
                                locationStore.saveLoc(context, enabled = true)

                                locationStore.saveDetails(context, latitude, longitude)
                                //might need to add function to retrieve the details

                            }
                            locationStore.getLocation.collect { enabled ->

                                locationPermissionGranted.value = enabled}
                        }}



                }
            })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),

        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "We would like to access your Location ",

                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = " Enabling location would improve your experience by giving you accurate information,.",

                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            if (hasLocationPermission(context)) {
                                // Permission already granted, update the location
                                getCurrentLocation(context) { lat, long ->

                                    coroutine.launch {
                                        withContext(Dispatchers.IO) {
                                            locationStore.saveLoc(context, enabled = true)

                                            locationStore.saveDetails(context, lat, long)


                                        }
                                        locationStore.getLocation.collect { enabled ->

                                            locationPermissionGranted.value = enabled}

                                    }
                                }
                                 if(userNameText.value=="User"){context.startActivity(Intent(context,LogoPage::class.java))}
                                else{context.startActivity(Intent(context,MainActivity::class.java))}
                            } else {
                                // Request location permission
                                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }

                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(text = "Allow")
                    }
                    Button(
                        onClick = {
                            context.startActivity(Intent(context, Settings::class.java))
                            coroutine.launch {
                                withContext(Dispatchers.IO) {
                                    locationStore.saveLoc(context, enabled = false)}}
                        }
                    ) {
                        Text(text = "Don't Allow")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

private fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

private fun getCurrentLocation(context: Context, callback: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {

        return
    }
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                callback(latitude, longitude)
            }
        }
        .addOnFailureListener { exception ->
            
            exception.printStackTrace()
        }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Location() {
    WeatherAppTheme {
        val context= LocalContext.current

        LocationScreen(locationStore = LocationStore(context), userStore = UserStore(context))
    }
}

