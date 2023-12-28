package com.example.weatherapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun WeatherCard(
    cityName: String,
    weatherDescription: String,
    temperatureRange: String
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.Transparent, shape = RoundedCornerShape(10.dp)),
//        elevation = 0.dp // Set elevation to 0 to remove shadow if not needed
    ) {
        Column { // Use a Column to stack the image Box and other content
            Box(
                modifier = Modifier
                    .height(250.dp) // Set the height of the Box to match the image
                    .fillMaxWidth() // The Box should fill the width of the Card
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
                        .align(Alignment.BottomStart) // Align Column to the bottom start of the Box
                        .padding(start = 16.dp, bottom = 16.dp) // Apply padding to position the text within the image
                ) {
                    Text(
                        text = "Tempreture: $temperatureRange",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(90.dp)) // Space between temperature range and weather description
                    Text(
                        text = weatherDescription,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(10.dp)) // Space between weather description and city name
                    Text(
                        text = cityName,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )
                }
            }

            // Other content of the card can go here
        }
    }
}