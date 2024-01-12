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
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SearchView : ComponentActivity() {
    private val viewModel: WeatherViewModel by viewModels()
    private val cityStore: CityStore by lazy { CityStore(context = this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchViewPreview(viewModel, cityStore)
        }
    }
}

class WeatherViewModel : ViewModel() {
    private val apiService = RetrofitInstance.create()
    private val _weatherData = MutableLiveData<List<CurrentWeather>>(listOf())
    val weatherData: LiveData<List<CurrentWeather>> = _weatherData
    private val apiKey: String = "63a7e436b523ae004cb898b99918ff61"
    private val _selectedCity = MutableLiveData<String?>()

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery
    // Function to update LiveData with a new weather item
    fun updateWeatherData(newWeather: CurrentWeather) {
        _weatherData.value = _weatherData.value?.plus(newWeather)
    }
    fun fetchWeatherData(city: String) {
        viewModelScope.launch {
            try {
                _weatherData.value = listOf()
                val response = apiService.getCurrentWeather(city, "metric", apiKey)
                if (response.isSuccessful && response.body() != null) {
                    _weatherData.value = listOf(response.body()!!)
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
                    val weatherData = response.body()!!
                    // Extract additional data
                    val humidity = weatherData.main.humidity
                    val sunrise = weatherData.sys.sunrise
                    val sunset = weatherData.sys.sunset
                    val windSpeed = weatherData.wind.speed
                    val airPressure = weatherData.main.pressure
                    // Call onResult with the weather data
                    onResult(weatherData)
                }
            } catch (e: Exception) {
                // Handle exceptions
            }
        }
    }

    init {
        searchQuery.observeForever { newQuery ->
            if (newQuery.isNotEmpty()) {
                fetchWeatherData(newQuery)
            }
        }
    }
}
@Composable
fun SearchViewBackArrowButton(context: Context) {
    IconButton( // navigating to main view
        onClick = {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    ) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchViewSearchBar(viewModel: WeatherViewModel, onQueryChanged: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val context = LocalContext.current

    OutlinedTextField(
        value = query,
        onValueChange = {
            query = it
            onQueryChanged(it)
            active = it.isNotEmpty()
        },
        label = { Text("Enter city name") },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            if (query.isNotEmpty()) {
                    viewModel.fetchWeatherDataForPreview(query) { weatherData ->
                    // Get the temperature range from the fetched weather data
                    val temperatureRange = "${weatherData.main.temp_min.toInt()}°C - ${weatherData.main.temp_max.toInt()}°C"
                    // Extract additional weather details
                    val humidity = "${weatherData.main.humidity}%"
                    val sunrise = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(weatherData.sys.sunrise * 1000L))
                    val sunset = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(weatherData.sys.sunset * 1000L))
                    val windSpeed = "${weatherData.wind.speed} m/s"
                    val airPressure = "${weatherData.main.pressure} hPa"
                    // Create the Intent to start MainActivity and pass the temperature range
                    val intent = Intent(context, MainActivity::class.java).apply {
                        putExtra("CITY_NAME_KEY", weatherData.name) // Pass city name to MainActivity
                        putExtra("TEMPERATURE_RANGE_KEY", temperatureRange) // Pass temperature range to MainActivity
                        putExtra("HUMIDITY_KEY", humidity)
                        putExtra("SUNRISE_KEY", sunrise)
                        putExtra("SUNSET_KEY", sunset)
                        putExtra("WIND_SPEED_KEY", windSpeed)
                        putExtra("AIR_PRESSURE_KEY", airPressure)
                    }

                    // Start MainActivity
                    context.startActivity(intent)
                    active = false
                }
            }
        }),
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
        },
        trailingIcon = {
            Row {
                IconButton(
                    onClick = {
                        query = ""
                        active = false
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}

@Composable
fun SearchViewPreview(viewModel: WeatherViewModel, cityStore: CityStore) {
    val weatherList by viewModel.weatherData.observeAsState(listOf())
    var showDeleteDialog by remember { mutableStateOf(false) }
    var previewQuery by remember { mutableStateOf("") }
    var previewWeather by remember { mutableStateOf<CurrentWeather?>(null) }
//    val cities = cityStore.getCities()
    val cities by cityStore.observeCities().collectAsState(initial = emptyList())

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
                if (previewWeather != null && previewQuery.isNotEmpty()) {
                    WeatherCard(
                        cityName = (previewWeather as? CurrentWeather)?.name ?: "",
                        temperatureRange = "${(previewWeather as? CurrentWeather)?.main?.temp_min?.toInt()}°C - ${(previewWeather as? CurrentWeather)?.main?.temp_max?.toInt()}°C",
                        weatherDescription = (previewWeather as? CurrentWeather)?.weather?.firstOrNull()?.description?.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                        } ?: "",
                        onCardSelected = {} // Add your logic for card selection here if needed
                    )
                }
                // Place Add and Remove buttons here
                AddRemoveWeatherButtons(
                    onAddClicked = {
                        if (previewQuery.isNotEmpty()) {
                            // Fetch data for the city
                            viewModel.fetchWeatherDataForPreview(previewQuery) { weatherData ->
                                // Save the fetched CurrentWeather object to the CityStore
                                cityStore.saveCity(weatherData)
                                // Update the LiveData so that the UI gets notified
                                // Check if the city is in the CityStore
                                val isCityAdded = cityStore.getCities().any { it.name == weatherData.name }
                                if (isCityAdded) {
                                    // City successfully added, update the LiveData so that the UI gets notified
                                    viewModel.updateWeatherData(weatherData)
                                }
                            }
                        }
                    },
                    onRemoveClicked = {
                        showDeleteDialog = true
                    }
                )
                if (showDeleteDialog) {
                    DeleteLocationDialog(
                        cityStore = cityStore, // Pass the list of cities from CityStore
                        onDismiss = { showDeleteDialog = false },
                        onDeleteLocation = { location ->
                            viewModel.removeCity(location)
                            cityStore.removeCity(location)
                            showDeleteDialog = false
                        }
                    )
                }
                // Usage in LazyColumn
                LazyColumn {
                    items(cities) { weather ->
                        WeatherCard(
                            cityName = weather.name,
                            temperatureRange = "${weather.main.temp_min.toInt()}°C - ${weather.main.temp_max.toInt()}°C",
                            weatherDescription = weather.weather.first().description.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
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
    cityStore: CityStore,//List<CurrentWeather>,
    onDismiss: () -> Unit,
    onDeleteLocation: (String) -> Unit
) {
    val cities by cityStore.observeCities().collectAsState(initial = emptyList())
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Delete Location") },
        text = {
            LazyColumn {
                items(cities) { city ->
                    Text(
                        text = city.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable { onDeleteLocation(city.name) }
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
