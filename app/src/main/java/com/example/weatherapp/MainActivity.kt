package com.example.weatherapp
import androidx.compose.ui.Alignment
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.style.BackgroundColorSpan
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.ui.theme.WeatherAppTheme

import androidx.compose.material3.BottomAppBar
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape


import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetDefaults.SheetPeekHeight
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background)
                {
                    PurpleActivityMaterial3()
                }
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurpleActivityMaterial3() {
    //val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    //val sheetState = rememberModalBottomSheetState()
    var optionalHandlerClicked by remember { mutableStateOf(false) }
    var currentListState = remember { mutableStateOf(hours) }
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    val sheetPeekHeight = 550.dp
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()


    BottomSheetScaffold(
        sheetShadowElevation = 80.dp,

        scaffoldState = bottomSheetScaffoldState,

         // Set the initial peek height to 300dp
        // Set the expanded height to 600dp
        sheetPeekHeight =  280.dp,
        //sheetPeekHeight = SheetPeekHeight. 300.dp,(peekHeight = 300.dp, expandHeight = 600.dp),
        sheetContent = {
            // Add hour-by-hour forecast content

                // Content when the bottom sheet is expanded
                Column (
                    //verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier= Modifier
                        .fillMaxWidth()
                        .background(Color(0xA3F6EDFC)),

                ){
                    Column(modifier=Modifier.height(sheetPeekHeight))
                    {
                        Row( verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 40.dp)) {
                            ElevatedButton(onClick = {currentListState.value= weeks


                                                     },modifier= Modifier
                                .width(95.dp)
                                .height(35.dp)) {
                                Text("Weekly",modifier=Modifier.width(90.dp))
                            }
                            (Spacer(modifier = Modifier.width(120.dp)))
                            ElevatedButton(onClick = { currentListState.value= hours},modifier= Modifier
                                .width(90.dp)
                                .height(35.dp)) {
                                Text("Daily",modifier=Modifier.width(90.dp))}
                        }
                        if (optionalHandlerClicked) {
                            println(optionalHandlerClicked)
                            run { scope.launch { scaffoldState.bottomSheetState.expand() } }
                        } else {
                            println(optionalHandlerClicked)
                            run { scope.launch { scaffoldState.bottomSheetState.partialExpand() } }
                        }


                       //run { scope.launch { scaffoldState.bottomSheetState.partialExpand() }}
                        (Spacer(modifier = Modifier.height(20.dp)))
                        //App()
                        intermidiete(currentListState)
                        (Spacer(modifier = Modifier.height(20.dp)))
                        Box( modifier= Modifier
                            .width(390.dp)
                            .background(Color.Red)
                            .height(800.dp)
                            .padding(30.dp)) {


                          LazyColumnWithCards()
                        }

                    }

                    // Add expanded forecast details



                }

        },


        sheetSwipeEnabled = true,
        sheetShape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
        sheetContainerColor = Color(0xA3F6EDFC),

        topBar = {
            TopAppBar(
                title = { Text(text = "Title") },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Ellipsis"
                        )
                    }
                }
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFD0BCFF))){


            Column(
                modifier = Modifier
                    .width(3500.dp)
                    .height(195.dp)
                    .padding(start = 18.dp, end = 16.dp, bottom = 10.dp, top = 7.dp),


                //.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Bristol",
                    Modifier
                        .width(200.dp)
                        .height(41.dp),
                    style = TextStyle(
                        fontSize = 34.sp,
                        lineHeight = 20.sp,
                        //fontFamily = FontFamily(Font(R.font.amaranth)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFFF7F2FA),
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.37.sp,
                    )
                )
                // Add weather details content
            }
        }
        }

    )

}

//val hours = listOf("9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM")
@Composable
// this is for the row for daily forecast
fun HourlySchedule(hours:List<String>) {

        LazyRow(contentPadding = PaddingValues(horizontal = 4.dp),
        //reverseLayout=true,
        userScrollEnabled = true
            )

        {
            itemsIndexed(hours) { index, hour ->
                Card(
                    modifier = Modifier
                        .width(90.dp)
                        .height(140.dp)

                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    ),

                    ) {
                    Text(
                        text = hour,
                        modifier = Modifier.padding(16.dp),

                        )
                }
            }
        }
    }


val hours = listOf("9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM")
val weeks = listOf("Mon","Tue","Wed")
@Composable
fun App(hours: List<String>){

    //val hours = listOf("9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM")
    HourlySchedule(hours = hours)

}
@Composable
fun intermidiete(currentListState: MutableState<List<String>>){

    App(currentListState.value)


}
// Call the SecondComposable function and pass the currentList


@Composable
fun CardItem(icon: ImageVector, text: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null, // Add appropriate content description
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text)
        }
    }
}

@Composable
fun LazyColumnWithCards() {
    val cardItems = listOf(
        CardItemData(icon = Icons.Filled.Home , text = "Home"),
        CardItemData(icon = Icons.Default.Favorite, text = "Favorite"),
        CardItemData(icon = Icons.Default.Settings, text = "Settings"),
        CardItemData(icon = Icons.Default.Person, text = "Profile"),
        CardItemData(icon = Icons.Default.Search, text = "Search")
    )

    LazyColumn {
        items(cardItems) { item ->
            CardItem(icon = item.icon, text = item.text)
        }
    }
}

data class CardItemData(val icon: ImageVector, val text: String)







@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        PurpleActivityMaterial3()
    }
}