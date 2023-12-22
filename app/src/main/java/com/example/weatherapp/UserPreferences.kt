package com.example.weatherapp
import android.content.Intent
import android.os.Bundle


import androidx.compose.material3.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.ui.theme.WeatherAppTheme


class UserPreferences : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFB88DD3)),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Images()

                }
            }
        }


    }

    //here add private function
    private fun navigateToMainPageAfterDelay() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}

@Composable
fun greeting():Boolean {
    var eventTrig by remember { mutableStateOf(false) }
    eventTrig = true

    return eventTrig

}
@Composable
fun Images() {

    val context = LocalContext.current
    val image = painterResource(R.drawable.logo_foreground)
    Column(
        modifier = Modifier.fillMaxSize()
            .background(color= Color(0xFFB88DD3)),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = image,
            contentDescription = "Icon",
            modifier = Modifier
                .size(700.dp)
                .background(color = Color(0xFFB88DD3))
        )
        Button(onClick = { context.startActivity(Intent(context, MainActivity::class.java)) }) {
            Text(text = "Go to Main Page")
        }

    }
}
@Preview(showBackground = true,showSystemUi = true)
@Composable
fun GreetingPreview2() {
    WeatherAppTheme {
        greeting()
    }
}
@Preview(showBackground = true,showSystemUi = true)
@Composable
fun ImagesPreview() {
    WeatherAppTheme {
        Images()
    }
}