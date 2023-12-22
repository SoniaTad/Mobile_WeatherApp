package com.example.weatherapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class RadioButtonOption {
    LessThanTwenty,
    LessThanTen,
    LessThanFive
}
class UserStore(private val context: Context) {
    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userPref")
        val User = stringPreferencesKey("user_name")
        val q1 =   stringPreferencesKey("radio_button_option")


    }
    // read operation
    //val user_nameFlow: Flow<Preferences> = context.dataStore.data
    val getPref: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[User] ?: RadioButtonOption.LessThanTen.name
        }
}

class LogoPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                    .background(Color.Blue),
                    //color = MaterialTheme.colorScheme.background
                ) {
                    UserPreferencesDialog()
                }
            }
        }
    }
}


    // creating a class that takes the user's name and their answer and the question to ask



@Composable
fun UserPreferencesDialog(
     question1: String = stringResource(R.string.Question1),

//onSavePreferences:( preferences:UserPreferences) -> Unit
) {
    val radioButtonOption = remember { mutableStateOf(RadioButtonOption.LessThanTen) }
    var userName by remember { mutableStateOf("") }
    var yesNoQuestion by remember { mutableStateOf("") }
    var userResponse by remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = {},
        modifier = Modifier.padding(start = 60.dp,top= 90.dp,end=30.dp,bottom=50.dp)
            .size(600.dp)
            .width(1000.dp)
            .height(700.dp),
        title = { Text(text = "User Preferences") },
        text = {
            Column {
                Text(text="Insert your name")
                (Spacer(modifier = Modifier.height(10.dp)))
                TextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("Name") },
                    //keyboardOptions =

                )
                Column{
                    (Spacer(modifier = Modifier.height(10.dp)))

                    Text(text=question1)
                Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = radioButtonOption.value == RadioButtonOption.LessThanTen,
                    onClick = { radioButtonOption.value = RadioButtonOption.LessThanTen}

                    )
                    Text(text = "Less than 10 degrees c")}
                Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = radioButtonOption.value == RadioButtonOption.LessThanFive,
                    onClick = { radioButtonOption.value = RadioButtonOption.LessThanFive}

                )
                Text(text = "Less than 5 degrees C ")}
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = radioButtonOption.value == RadioButtonOption.LessThanTwenty,
                        onClick = { radioButtonOption.value = RadioButtonOption.LessThanTwenty}

                    )
                    Text(text = "Less than 20 degrees C ")}
                    }



            }
        },
        confirmButton = {
            Button(
                onClick = {
                    //val userPreferences = UserPreferences(preferences)
                    //onSavePreferences(userP)
                    openDialog.value = false
                }
            ) {
                Text(text = "Save")
            }
        },

    )
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview3() {
    WeatherAppTheme {
        UserPreferencesDialog()
    }
}