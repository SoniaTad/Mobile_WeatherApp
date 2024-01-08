package com.example.weatherapp
// Contents of the file (e.g., class, object, function)


import WeatherCard
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.models.CurrentWeather
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.utils.RetrofitInstance
import kotlinx.coroutines.launch
import java.util.Locale


class SearchView : ComponentActivity() {
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchViewPreview(viewModel)
        }
    }
}

class WeatherViewModel : ViewModel() {
    private val apiService = RetrofitInstance.create()
    private val _weatherData = MutableLiveData<CurrentWeather>()
    val weatherData: LiveData<CurrentWeather> = _weatherData
    private val apiKey: String = "63a7e436b523ae004cb898b99918ff61"

//    Need to work out how to add more than one card
//    Nee dto work out how to add and remove with the buttons so that they are interactive with the cards
    fun fetchWeatherData(city: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getCurrentWeather(city, "metric", apiKey)
                if (response.isSuccessful && response.body() != null) {
                    _weatherData.value = response.body()
                } else {
                    // Handle errors
                }
            } catch (e: Exception) {
                // Handle exceptions
            }
        }
    }
//    fun searchCity(cityName: String) {
//        viewModelScope.launch {
//            try {
//                val response = apiService.getCurrentWeather(cityName, "metric", apiKey)
//                if (response.isSuccessful && response.body() != null) {
//                    _weatherData.value = response.body()
//                } else {
//                    // Log error or update an error LiveData to show a message to the user
//                }
//            } catch (e: Exception) {
//                // Log error or update an error LiveData to show a message to the user
//            }
//        }
//    }

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

//@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchViewSearchBar(viewModel: WeatherViewModel) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = query,
        onValueChange = { newText -> query = newText },
        label = { Text("Enter city name") },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            if (query.isNotEmpty()) {
                viewModel.fetchWeatherData(query)
                active = false
                query = "" // Clear the search bar after submitting the query
            }
        }),
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { query = "" }) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear")
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}
//@Preview
//@Composable
//fun SearchViewPreview(viewModel: WeatherViewModel) {
//    val weather by viewModel.weatherData.observeAsState()
//
//    WeatherAppTheme(darkTheme = false) {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                horizontalAlignment = Alignment.Start
//            ) {
//                SearchViewBackArrowButton(context = LocalContext.current)
//                // Pass the lambda to SearchViewSearchBar
//                SearchViewSearchBar()
//
//                // Display the weather information
//                weather?.let { weather ->
//                    WeatherCard(
//                        cityName = weather.name,
//                        temperatureRange = "${weather.main.temp}°C",
//                        weatherDescription = weather.weather.first().description
//                    )
//                }
//            }
//        }
//    }
//}

//@Preview(showBackground = true)
@Composable
fun SearchViewPreview(viewModel: WeatherViewModel) {
    val weather by viewModel.weatherData.observeAsState()
    val cityName: String = "berlin"

    WeatherAppTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                SearchViewBackArrowButton(context = LocalContext.current)
                SearchViewSearchBar(viewModel) // This will trigger searchCity in the ViewModel

                // Update WeatherCard with the latest weather data
                weather?.let {
                    WeatherCard(
                        cityName = it.name,
                        temperatureRange = "${it.main.temp_min.toInt()}°C - ${it.main.temp_max.toInt()}°C",
                        weatherDescription = it.weather.first().description.capitalize(Locale.ROOT)
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MyPreviewPreview() {
// SearchViewPreview()
//}
