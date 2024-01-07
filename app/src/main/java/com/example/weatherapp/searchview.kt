package com.example.weatherapp
// Contents of the file (e.g., class, object, function)


import WeatherCard
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.ui.theme.WeatherAppTheme


class SearchView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchViewPreview()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchViewPreview() {
    WeatherAppTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                SearchViewBackArrowButton(context = LocalContext.current) // Use the renamed function
                SearchViewSearchBar() // Use the renamed function

                // Display WeatherCard with hardcoded sample data
                WeatherCard(
                    cityName = "Sample City",
                    temperatureRange = "20°C - 25°C",
                    weatherDescription = "Sunny"
                )
            }
        }
    }
}

@Composable
fun SearchViewBackArrowButton(context: Context) {
    IconButton(
        onClick = {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    ) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchViewSearchBar() { // Renamed to SearchViewSearchBar
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    DockedSearchBar(
        query = query, // The current search query text
        onQueryChange = { query = it }, // Callback invoked when the user changes the search query.
        onSearch = {   //  Callback invoked when the user initiates a search
            active = false
            query =""
        },
        active = active, // Whether the search bar is currently active.
        onActiveChange = { active = it }, // Callback invoked when the active state changes.
        placeholder = { // Text to be displayed as a placeholder when the search bar is empty.
            Text(text = "Search")
        },
        leadingIcon = { // Icon displayed on the left side of the search bar.
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
        },
        trailingIcon = { //  Icons or buttons displayed on the right side of the search bar.
            Row {
                if (active) {
                    IconButton(
                        onClick = { if (query.isNotEmpty()) query = "" else active = false }
                    ) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                    }
                }
            }
        },
        modifier = Modifier
            .padding(start = 16.dp)
    ) {
    }
}

//class WeatherViewModel(private val apiService: WeatherApiService) : ViewModel() {
//    private val _weatherData = MutableLiveData<CurrentWeather>()
//    val weatherData: LiveData<CurrentWeather> = _weatherData
//
//    fun fetchWeatherData(city: String, apiKey: String) {
//        viewModelScope.launch {
//            try {
//                val response = apiService.getCurrentWeather(city, "metric", apiKey)
//                if (response.isSuccessful && response.body() != null) {
//                    _weatherData.value = response.body()
//                } else {
//                    // Handle errors
//                }
//            } catch (e: Exception) {
//                // Handle exceptions
//            }
//        }
//    }
//}

//@Composable
//fun SearchViewPreview(viewModel: WeatherViewModel) {
//
//    val context = LocalContext.current
//    val weather by viewModel.weatherData.observeAsState()
//
//    // Assuming you have a way to input city name
//    val cityName = "bristol" // Replace with actual input
//    val apiKey = "63a7e436b523ae004cb898b99918ff61" // Replace with your actual API key
//
//    LaunchedEffect(cityName) {
//        viewModel.fetchWeatherData(cityName, apiKey)
//    }
//
//    WeatherAppTheme {
//        WeatherAppTheme(darkTheme = false) {
//            Surface(
//                modifier = Modifier.fillMaxSize(),
//                color = MaterialTheme.colorScheme.background
//            ) {
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    horizontalAlignment = Alignment.Start
//                ) {
//                    SearchViewBackArrowButton(context = LocalContext.current) // Use the renamed function
//                    SearchViewSearchBar() // Use the renamed function
//
//                    weather?.let {
//                        WeatherCard(
//                            cityName = it.name,
//                            temperatureRange = "${it.main.temp}°C",
//                            weatherDescription = it.weather.first().description
//                        )
//                    }
//                }
//            }
//        }
//    }
//}

//@Composable
//fun MyPreview() {
//    WeatherAppTheme(darkTheme = false) {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                horizontalAlignment = Alignment.Start
//            ) {
//                BackArrowButton(context = LocalContext.current)
//                SearchBar()
//
//                // Display WeatherCard with empty strings
//                WeatherCard(
//                    cityName = cityName,
//                    temperatureRange = temperatureRange,
//                    weatherDescription = weatherDescription
//                )
//            }
//        }
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun MyPreviewPreview() {
// SearchViewPreview()
//}
