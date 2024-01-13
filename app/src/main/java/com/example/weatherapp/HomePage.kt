package com.example.weatherapp


import LocationStore
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class HomePage : ComponentActivity() {
    private val viewModel: WeatherViewModel by viewModels()
    private val locationStore by lazy { LocationStore(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                val weatherData by viewModel.weatherData.observeAsState(initial = listOf())
                var cityName by remember { mutableStateOf("Loading...") }
                var temperatureRange by remember { mutableStateOf("N/A") }
                var humidity by remember { mutableStateOf("N/A") }
                var sunrise by remember { mutableStateOf("N/A") }
                var sunset by remember { mutableStateOf("N/A") }
                var windSpeed by remember { mutableStateOf("N/A") }
                var airPressure by remember { mutableStateOf("N/A") }

                val searchedCityName = intent.getStringExtra("CITY_NAME_KEY")
                if (searchedCityName != null) {
                    viewModel.updateWeatherDataFromSearch(searchedCityName)
                } else {
                    val latitude = locationStore.getLatitude.collectAsState(initial = 0.0).value
                    val longitude = locationStore.getLongitude.collectAsState(initial = 0.0).value
                    if (latitude != 0.0 && longitude != 0.0) {
                        viewModel.fetchWeatherDataWithCoordinates(latitude, longitude)
                    }
                }

                if (weatherData.isNotEmpty()) {
                    val currentWeather = weatherData.first()
                    cityName = currentWeather.name
                    temperatureRange = "${currentWeather.main.temp_min.toInt()}°C - ${currentWeather.main.temp_max.toInt()}°C"
                    humidity = "${currentWeather.main.humidity}%"
                    sunrise = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(currentWeather.sys.sunrise * 1000L))
                    sunset = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(currentWeather.sys.sunset * 1000L))
                    windSpeed = "${currentWeather.wind.speed} m/s"
                    airPressure = "${currentWeather.main.pressure} hPa"
                }

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    PurpleActivityMaterial3(locationStore, cityName, temperatureRange, humidity, sunrise, sunset, windSpeed, airPressure)
                }
            }
        }
    }
}




@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurpleActivityMaterial3(locationStore: LocationStore,cityName: String, temperatureRange: String, humidity: String, sunrise: String, sunset: String, windSpeed: String, airPressure: String) {
    //val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    //val sheetState = rememberModalBottomSheetState()
    val latitude = remember { mutableDoubleStateOf(0.0) }
    val longitude = remember { mutableDoubleStateOf(0.0) }
    LaunchedEffect(Unit) {
        locationStore.getLatitude.collect { lat ->
            latitude.value = lat
        }
    }
    LaunchedEffect(Unit) {
        locationStore.getLongitude.collect { long ->
            longitude.value = long
        }
    }
    var optionalHandlerClicked by remember { mutableStateOf(false) }
    var currentListState = remember { mutableStateOf(hours) }
    var currentListState2 = remember { mutableStateOf(hours_temp) }
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val sheetPeekHeight = 550.dp
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()


    BottomSheetScaffold(
        sheetShadowElevation = 80.dp,

        scaffoldState = bottomSheetScaffoldState,


        sheetPeekHeight =  280.dp,
        sheetContent = {

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier= Modifier
                    .fillMaxWidth()
                    .background(Color(0xA3F6EDFC)),

                ){
                Column(modifier=Modifier.height(sheetPeekHeight))
                {
                    Row( verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 40.dp)) {
                        ElevatedButton(onClick = {currentListState.value= weeks
                            currentListState2.value= week_temp


                        },modifier= Modifier
                            .width(95.dp)
                            .height(35.dp)) {
                            Text("Weekly",modifier=Modifier.width(90.dp))
                        }
                        (Spacer(modifier = Modifier.width(120.dp)))
                        ElevatedButton(onClick = { currentListState.value= hours
                            currentListState2.value= hours_temp},modifier= Modifier
                            .width(90.dp)
                            .height(35.dp)) {
                            Text("Daily",modifier=Modifier.width(90.dp))}
                    }
                    if (optionalHandlerClicked) {
                        println(optionalHandlerClicked)
                        run { scope.launch { scaffoldState.bottomSheetState.expand() } }
                    } else {
                        println(optionalHandlerClicked)
                        run { scope.launch { scaffoldState.bottomSheetState.partialExpand() } }
                    }


                    //run { scope.launch { scaffoldState.bottomSheetState.partialExpand() }}
                    (Spacer(modifier = Modifier.height(20.dp)))

                    intermidiete(currentListState,currentListState2)
                    (Spacer(modifier = Modifier.height(20.dp)))
                    Box( modifier= Modifier
                        .width(390.dp)
                        .background(Color(0xFFA59AC0))
                        .height(800.dp)
                        .padding(30.dp)) {


                        LazyColumnWithCards(humidity, sunrise, sunset, windSpeed, airPressure)
                    }

                }
            }
        },


        sheetSwipeEnabled = true,
        sheetShape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
        sheetContainerColor = Color(0xA3F6EDFC),

        topBar = {
            TopAppBar(
                title = { Text(text = "Search") },
                actions = {
                    IconButton(onClick = { context.startActivity(Intent(context, Settings::class.java))}) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        // Start SearchView Activity when the IconButton is clicked
                        context.startActivity(Intent(context, SearchView::class.java))
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Cloudy"
                        )
                    }
                }
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFD0BCFF))){


                Column(
                    modifier = Modifier
                        .width(3500.dp)
                        .height(300.dp)
                        .padding(start = 18.dp, end = 16.dp, bottom = 10.dp, top = 7.dp),


                    //.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = cityName,
                        Modifier
                            .width(300.dp)
                            .height(60.dp),
                        style = TextStyle(
                            fontSize = 40.sp,
                            lineHeight = 20.sp,
                            //fontFamily = FontFamily(Font(R.font.amaranth)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFFF7F2FA),
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.37.sp,
                        )
                    )

                    Text(
                        text = "Temperature: $temperatureRange",
                        // Styling for the temperature text
                        Modifier
                            .width(200.dp)
                            .height(50.dp),
                        style = TextStyle(
                            fontSize = 20.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight(400),
                            color = Color(0xFFF7F2FA),
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.37.sp,
                        )
                    )
                }

            }
        }

    )

}

//val hours = listOf("9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM")
@Composable
// this is for the row for daily forecast
fun HourlySchedule(hours:List<String>,hours_temp:List<String>) {

    LazyRow(contentPadding = PaddingValues(horizontal = 4.dp),
        //reverseLayout=true,
        userScrollEnabled = true
    )

    {
        itemsIndexed(hours.zip(hours_temp)) { index, (hour, temp) ->
            Card(
                modifier = Modifier
                    .width(90.dp)
                    .height(140.dp)

                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                ),

                ) {
                Text(
                    text = hour,
                    modifier = Modifier.padding(16.dp),

                    )
            }
        }
    }
}


val hours = listOf("00 AM", "03 AM", "06 AM", "09 AM", "12 PM", "03 PM", "06 PM")
val weeks = listOf("Mon","Tue","Wed")
val hours_temp= listOf("3","3","3","3","3","3","3")
val week_temp= listOf("4","4","5","6","3","3","3")
@Composable
fun App(hours: List<String>,hours_temp: List<String>){

    //val hours = listOf("9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM")
    HourlySchedule(hours = hours,hours_temp=hours_temp)

}
@Composable
fun intermidiete(currentListState: MutableState<List<String>>,currentListState2:MutableState<List<String>>){

    App(currentListState.value,currentListState2.value)


}


@Composable
fun CardItem(icon: Painter, text: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon,
                contentDescription = null, // Add appropriate content description
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text)
        }
    }
}

@Composable
fun LazyColumnWithCards(humidity: String, sunrise: String, sunset: String, windSpeed: String, airPressure: String) {
    val cardItems = listOf(
        CardItemData(icon = painterResource(id = R.drawable.ic_wind), text = "Wind Speed: $windSpeed"),
        CardItemData(icon = painterResource(id = R.drawable.ic_sunrise), text = "Sunrise: $sunrise"),
        CardItemData(icon = painterResource(id = R.drawable.ic_sunset), text = "Sunset: $sunset"),
        CardItemData(icon = painterResource(id = R.drawable.ic_pressure), text = "Air Pressure: $airPressure"),
        CardItemData(icon = painterResource(id = R.drawable.ic_humidity), text = "Humidity: $humidity")
        // Add weather details content
    )

    LazyColumn {
        items(cardItems) { item ->
            CardItem(icon = item.icon, text = item.text)
        }
    }
}

data class CardItemData(val icon: Painter, val text: String)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        val context = LocalContext.current
        PurpleActivityMaterial3(locationStore = LocationStore(context),cityName = "Sample City", temperatureRange = "0 - 1",sunrise = "00:00",sunset = "00:00", windSpeed = "0", airPressure = "0", humidity = "")
    }
}
