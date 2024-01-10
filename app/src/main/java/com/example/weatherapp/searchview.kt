package com.example.weatherapp
// Contents of the file (e.g., class, object, function)


import AddRemoveWeatherButtons
import WeatherCard
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
    private val _weatherData = MutableLiveData<List<CurrentWeather>>(listOf())
    val weatherData: LiveData<List<CurrentWeather>> = _weatherData
    private val apiKey: String = "63a7e436b523ae004cb898b99918ff61"

    private val _selectedCity = MutableLiveData<String?>()
//    val selectedCity: LiveData<String?> = _selectedCity

    //    Need to work out how to add more than one card
//    Nee dto work out how to add and remove with the buttons so that they are interactive with the cards
    fun fetchWeatherData(city: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getCurrentWeather(city, "metric", apiKey)
                if (response.isSuccessful && response.body() != null) {
                    _weatherData.value = _weatherData.value?.plus(response.body()!!)
                } else {
                    // Handle errors
                }
            } catch (e: Exception) {
                // Handle exceptions
            }
        }
    }

    fun selectCity(cityName: String) {
        _selectedCity.value = cityName
    }

//    fun removeSelectedCity() {
//        _selectedCity.value?.let { cityName ->
//            removeCity(cityName)
//            Log.d("WeatherCard", "Removed city: $cityName")
//            _selectedCity.value = null // Reset the selected city
//        }
//    }

    fun removeCity(cityName: String) {
        val updatedList = _weatherData.value?.filterNot { it.name == cityName }
        _weatherData.value = updatedList
        Log.d("WeatherCard", "Selected city: $cityName")
    }

    fun fetchWeatherDataForPreview(city: String, onResult: (CurrentWeather) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.getCurrentWeather(city, "metric", apiKey)
                if (response.isSuccessful && response.body() != null) {
                    onResult(response.body()!!)
                }
            } catch (e: Exception) {
                // Handle exceptions
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

//@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchViewSearchBar(viewModel: WeatherViewModel, onQueryChanged: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = query,
//        onValueChange = { newText -> query = newText },
        onValueChange = {
            query = it
            onQueryChanged(it)  // Notify the caller about the change
        },
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
    val weatherList by viewModel.weatherData.observeAsState(listOf())
    var showDeleteDialog by remember { mutableStateOf(false) }
    var previewQuery by remember { mutableStateOf("") }
    var previewWeather by remember { mutableStateOf<CurrentWeather?>(null) }

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
                SearchViewSearchBar(viewModel) { newQuery ->
                    previewQuery = newQuery
                    if (newQuery.isNotEmpty()) {
                        viewModel.fetchWeatherDataForPreview(newQuery) { weatherData ->
                            previewWeather = weatherData
                        }
                    } else {
                        previewWeather = null
                    }
                }
                // Show the preview card if there is data
                previewWeather?.let { weather ->
                    WeatherCard(
                        cityName = weather.name,
                        temperatureRange = "${weather.main.temp_min.toInt()}°C - ${weather.main.temp_max.toInt()}°C",
                        weatherDescription = weather.weather.first().description.capitalize(Locale.ROOT),
                        onCardSelected = {}
                    )
                }

                // Place Add and Remove buttons here
                AddRemoveWeatherButtons(
                    onAddClicked = {
                        // Define the action for the add button
                    },
                    onRemoveClicked = {
                        showDeleteDialog = true
                    }
                )
                if (showDeleteDialog) {
                    DeleteLocationDialog(
                        locations = weatherList, // Assuming this is your list of locations
                        onDismiss = { showDeleteDialog = false },
                        onDeleteLocation = { location ->
                            viewModel.removeCity(location)
                            showDeleteDialog = false
                        }
                    )
                }
                // Usage in LazyColumn
                LazyColumn {
                    items(weatherList) { weather ->
                        WeatherCard(
                            cityName = weather.name,
                            temperatureRange = "${weather.main.temp_min.toInt()}°C - ${weather.main.temp_max.toInt()}°C",
                            weatherDescription = weather.weather.first().description.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.ROOT
                                ) else it.toString()
                            },
                            onCardSelected = { selectedCityName ->
                                viewModel.selectCity(selectedCityName)
                                Log.d("WeatherCard", "Selected city: $selectedCityName")
                            }
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun DeleteLocationDialog(
    locations: List<CurrentWeather>,
    onDismiss: () -> Unit,
    onDeleteLocation: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Delete Location") },
        text = {
            LazyColumn {
                items(locations) { location ->
                    Text(
                        text = location.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable { onDeleteLocation(location.name) }
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}

//@Preview(showBackground = true)
//@Composable
//fun MyPreviewPreview() {
// SearchViewPreview()
//}
