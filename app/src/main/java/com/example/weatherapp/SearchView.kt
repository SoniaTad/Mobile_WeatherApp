package com.example.weatherapp

//newsearchview
// Contents of the file (e.g., class, object, function)
import com.example.weatherapp.CityStore
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
import androidx.compose.runtime.collectAsState
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
    private val cityStore: CityStore by lazy { CityStore(context = this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchViewPreview(viewModel, cityStore)
        }
        viewModel.loadFavoriteCities(cityStore)
    }




}




class WeatherViewModel : ViewModel() {
    private val apiService = RetrofitInstance.create()
    private val _weatherData = MutableLiveData<List<CurrentWeather>>(listOf())
    val weatherData: LiveData<List<CurrentWeather>> = _weatherData
    private val apiKey: String = "63a7e436b523ae004cb898b99918ff61"
    private val _favoriteCities = MutableLiveData<Set<String>>(setOf())
    val favoriteCities: LiveData<Set<String>> = _favoriteCities




    fun loadFavoriteCities(cityStore: CityStore) {
        viewModelScope.launch {
            _favoriteCities.value = cityStore.getFavoriteCities()
            Log.d("WeatherViewModel", "Loaded cities: ${_favoriteCities.value}")
        }
    }




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
    IconButton( // navigating to main view
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
                // Instead of fetching data immediately, notify the caller about the change
                onQueryChanged(query)
                active = false
            }
        }),
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    if (active) {
                        query = ""
                    } else {
                        // Perform an action when the search bar is not active
                        // For example, open a dialog, navigate to another screen, etc.
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = if (active) "Close" else "close"
                )
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
                            viewModel.fetchWeatherData(previewQuery)




                            // Save the added city to the store
                            viewModel.weatherData.value?.firstOrNull { it.name == previewQuery }?.let { weatherData ->
                                viewModel.viewModelScope.launch {
                                    cityStore.saveCity(weatherData.name)
                                }
                            }




                            // Optionally, you can select the city here
                            viewModel.selectCity(previewQuery)
                        }
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
//                fun clearAllCities() {
//                    viewModel.viewModelScope.launch {
//                        cityStore.clearCities()
//                    }
//                }
                // Usage in LazyColumn
                LazyColumn {
                    items(weatherList ?: emptyList()) { weather ->
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

