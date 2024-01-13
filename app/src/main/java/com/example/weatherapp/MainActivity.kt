package com.example.weatherapp


import LocationStore
import android.content.Intent
import android.os.Bundle
import androidx.compose.material3.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.ui.theme.WeatherAppTheme

import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val locationStore by lazy { LocationStore(this) }
    private val userStore by lazy { UserStore(this) }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFB88DD3)),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Images(locationStore,userStore)

                }
            }
        }


    }



}

@Composable
fun greeting():Boolean {
    var eventTrig by remember { mutableStateOf(false) }
    eventTrig = true

    return eventTrig

}
@Composable
fun Images(locationStore: LocationStore,userStore:UserStore) {
    val userNameText = remember { mutableStateOf("") }
    val radioButtonOptionText = remember { mutableStateOf(RadioButtonOption.LessThanTen) }

    val locationPermissionGranted = remember { mutableStateOf(true) }

    val latitude = remember { mutableDoubleStateOf(0.0) }
    val longitude= remember { mutableDoubleStateOf(0.0) }
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current
    // getting the location state to check whether to go to location view or logo page
    LaunchedEffect(Unit) {
        locationStore.getLocation.collect { enabled->

            locationPermissionGranted.value=enabled
        }
    }

    val image = painterResource(R.drawable.logo_foreground)

    Column(
        modifier = Modifier.fillMaxSize()
            .background(color= Color(0xFFB88DD3)),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = image,
            contentDescription = "Icon",
            modifier = Modifier
                .size(700.dp)
                .background(color = Color(0xFFB88DD3))
        )
        Button(onClick = { // getting the value of enabled and assign it to the variable
            // then decide which activity to go to next
            coroutine.launch {

                locationStore.getLatitude.collect{lat ->
                    latitude.doubleValue = lat }
                locationStore.getLongitude.collect{long ->
                    longitude.doubleValue = long }
                userStore.getPref.collect { (userName, radioButtonOption) ->
                    // Update the text values
                    userNameText.value = userName
                    radioButtonOptionText.value = radioButtonOption
                }


            }// if location is enabled then check whether lat and long aren't 0.0
            if(locationPermissionGranted.value)
            {
                if(latitude.doubleValue==0.0 && longitude.doubleValue==0.0)
                {
                    context.startActivity(Intent(context, EnableLocation::class.java))
                }
                else{ if(userNameText.value=="User"){
                    context.startActivity(Intent(context, UserPreferences::class.java))
                }else{
                    context.startActivity(Intent(context, HomePage::class.java))}
                }}
            else{  // location is not enabled
                context.startActivity(Intent(context, EnableLocation::class.java)) }
        }
        ) {
            Text(text = "Hello!")
        }

    }
}
@Preview(showBackground = true,showSystemUi = true)
@Composable
fun GreetingPreview2() {
    WeatherAppTheme {
        greeting()
    }
}
@Preview(showBackground = true,showSystemUi = true)
@Composable
fun ImagesPreview() {
    WeatherAppTheme {
        val context =  LocalContext.current
        Images(locationStore = LocationStore(context), userStore = UserStore(context ))
    }
}
