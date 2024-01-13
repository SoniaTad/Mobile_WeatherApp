
import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.weatherapp.R

@Composable
fun WeatherCard(
    cityName: String,
    weatherDescription: String,
    temperatureRange: String,
    onCardSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.Transparent)
            .clickable { onCardSelected(cityName) }
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
                    text = temperatureRange,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(90.dp))
                Text(
                    text = weatherDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = cityName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
            }
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
                }

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
                            Log.d("onCitySelected", "City has been selected")
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