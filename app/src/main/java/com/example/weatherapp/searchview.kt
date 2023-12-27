package com.example.weatherapp


import android.content.Context
import android.content.Intent
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun BackArrowButton(context: Context) {
    IconButton(
        onClick = {
            // Start MainActivity when the arrow is clicked
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    ) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
// SearchBar is designed to be used as a docked search bar, potentially for larger screens.
fun SearchBar() {
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
@Composable
fun MyPreview(context: Context = LocalContext.current) {
    WeatherAppTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                BackArrowButton(context = LocalContext.current)
                SearchBar()
            }
        }
    }
}
@Preview
@Composable
fun MyPreviewPreview() {
    MyPreview()
}