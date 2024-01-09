
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.weatherapp.R

//package com.example.weatherapp
//
//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Card
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//
//
//@Composable
//fun WeatherCard(
//    cityName: String,
//    weatherDescription: String,
//    temperatureRange: String
//) {
//    Card(
//        modifier = Modifier
//            .padding(8.dp)
//            .fillMaxWidth()
//            .background(Color.Transparent, shape = RoundedCornerShape(10.dp)),
////        elevation = 0.dp // Set elevation to 0 to remove shadow if not needed
//    ) {
//        Column { // Use a Column to stack the image Box and other content
//            Box(
//                modifier = Modifier
//                    .height(250.dp) // Set the height of the Box to match the image
//                    .fillMaxWidth() // The Box should fill the width of the Card
//                    .background(Color.Transparent)
//            ) {
//                // Background image
//                Image(
//                    painter = painterResource(id = R.drawable.ic_weather_windy),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color.Transparent),
//
//                    contentScale = ContentScale.FillWidth
//
//                )
//
//                // Texts positioned at the bottom left of the image
//                Column(
//                    modifier = Modifier
//                        .align(Alignment.BottomStart) // Align Column to the bottom start of the Box
//                        .padding(start = 16.dp, bottom = 16.dp) // Apply padding to position the text within the image
//                ) {
//                    Text(
//                        text = "Tempreture: $temperatureRange",
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = Color.White
//                    )
//                    Spacer(modifier = Modifier.height(90.dp)) // Space between temperature range and weather description
//                    Text(
//                        text = weatherDescription,
//                        style = MaterialTheme.typography.bodySmall,
//                        color = Color.White
//                    )
//                    Spacer(modifier = Modifier.height(10.dp)) // Space between weather description and city name
//                    Text(
//                        text = cityName,
//                        style = MaterialTheme.typography.headlineSmall,
//                        color = Color.White
//                    )
//                }
//            }
//
//            // Other content of the card can go here
//        }
//    }
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//@Preview(showBackground = true)
//fun WeatherCardPreview() {
//WeatherCard(
//cityName = "Sample City",
//weatherDescription = "Sample Weather Description",
//temperatureRange = "20°C - 25°C"
//)
//}

@Composable
fun WeatherCard(
    cityName: String,
    weatherDescription: String,
    temperatureRange: String,
) {
    var cardData by remember { mutableStateOf(listOf(WeatherData(cityName, weatherDescription, temperatureRange))) }
    var showDialog by remember { mutableStateOf(false) }

    Column {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            IconButton(
//                onClick = {
//                    cardData = cardData + WeatherData("New City", "New Description", "New Temperature")
//                }
//            ) {
//                Icon(imageVector = Icons.Default.Add, contentDescription = null)
//            }
//
//            IconButton(
//                onClick = {
//                    if (cardData.isNotEmpty()) {
//                        showDialog = true
//                    }
//                }
//            ) {
//                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
//            }
//        }
        cardData.forEach { data ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(Color.Transparent, shape = RoundedCornerShape(10.dp)),
            ) {
                Box(
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth()
                        .background(Color.Transparent)
                ) {
                    // Background image
                    Image(
                        painter = painterResource(id = R.drawable.ic_weather_windy),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent),
                        contentScale = ContentScale.FillWidth
                    )

                    // Texts positioned at the bottom left of the image
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp, bottom = 16.dp)
                    ) {
                        Text(
                            text = data.temperatureRange,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(90.dp))
                        Text(
                            text = data.weatherDescription,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = data.cityName,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                    }
                }
            }
        }


        if (showDialog) {
            CitySelectionDialog(
                cities = cardData.map { it.cityName },
                onDismiss = { showDialog = false },
                onCitySelected = { index ->
                    cardData = cardData.filterIndexed { i, _ -> i != index }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun AddRemoveWeatherButtons(
    onAddClicked: () -> Unit,
    onRemoveClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onAddClicked) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }

        IconButton(onClick = onRemoveClicked) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove")
        }
    }
}




@Composable
fun CitySelectionDialog(
    cities: List<String>,
    onDismiss: () -> Unit,
    onCitySelected: (Int) -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        content = {
            Box(
                modifier = Modifier.run {
                    padding(16.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                } // Use background color from the theme
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Select a city to delete")
                    Spacer(modifier = Modifier.height(16.dp))

                    cities.forEachIndexed { index, city ->
                        CitySelectionItem(city = city) {
                            onCitySelected(index)
                            onDismiss()
                        }
                        if (index < cities.size - 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun CitySelectionItem(city: String, onItemClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Check, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(city)
    }
}

data class WeatherData(
    val cityName: String,
    val weatherDescription: String,
    val temperatureRange: String
)