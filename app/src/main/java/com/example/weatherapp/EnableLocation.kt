import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority


class EnableLocation : ComponentActivity() {
    private val locationStore by lazy { LocationStore(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            LocationPermissionScreen(this,locationStore)
        }
    }
}

@Composable
fun LocationPermissionScreen(context:Context,locationStore:LocationStore) {
    val locationPermissionGranted = remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val coroutine = rememberCoroutineScope()
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            //the app has been granted the necessary permissions
            if (isGranted) {
                // Permission granted, proceed to the next activity
                //val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val location = if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    Toast.makeText(context, "I'm here ", Toast.LENGTH_SHORT).show()
                    coroutine.launch {
                        withContext(Dispatchers.IO) {
                            locationStore.saveLoc(context, enabled = false)
                        }
                        locationStore.getLocation.collect { enabled ->

                            locationPermissionGranted.value = enabled
                        }
                    }
                    // Ask for permission
                    return@rememberLauncherForActivityResult
                } else {



                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    //fusedLocationClient.lastLocation

                }
                //location.addOnSuccessListener {  lo ->}
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude


                    coroutine.launch {
                        withContext(Dispatchers.IO) {
                            locationStore.saveLoc(context, enabled = true)

                            locationStore.saveDetails(context,latitude,longitude)
                            //might need to add function to retrieve the details

                        }
                    }
                }else{ //if location is null
                    Toast.makeText(context, "Location is null ", Toast.LENGTH_SHORT).show()}

            } else {
                // Permission denied, send the user to another page
                // error message could not use location
                Toast.makeText(context, "Error permission has not been granted", Toast.LENGTH_SHORT).show()
                coroutine.launch {
                    withContext(Dispatchers.IO) {
                        locationStore.saveLoc(context, enabled = false)}}
                // send to search view
            }
        }
    )
    //if false
    if (!locationPermissionGranted.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Enable Location Services")
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "To continue using the app, please enable location services.")
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = { showDialog = true }) {
                    Text(text = "Enable Location")
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Enable Location Services") },
                text = { Text("Do you want to enable location services now or later?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            coroutine.launch {
                                withContext(Dispatchers.IO) {
                                    locationStore.saveLoc(context, enabled = true)}}
                            // send to search view
                        }
                    ) {
                        Text("Grant permission")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false
                        val settingsView = Intent(context, Settings::class.java)
                        context.startActivity(settingsView)}) {
                        Text("Don't grant permission")
                    }
                }
            )
        }
        // Show UI to ask for location permission

    } else {
        // Permission already granted, proceed to the next activity
        // Get the latitude and longitude here
        Button(onClick = {   val pass = Intent(context, MainActivity::class.java)
            context.startActivity(pass)}) {
            Text(text = "go to next activity")
        }
    }


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Location() {
    WeatherAppTheme {
        val context= LocalContext.current
        LocationPermissionScreen(context,LocationStore(context))
    }
}