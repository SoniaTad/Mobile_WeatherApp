package com.example.weatherapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    val getPref: Flow<Pair<String, RadioButtonOption>> = context.dataStore.data
        .map { preferences ->
            val radioButtonOption=RadioButtonOption.valueOf(preferences[q1] ?: RadioButtonOption.LessThanTen.name)
            val userName=preferences[User] ?:""
            Pair(userName, radioButtonOption)
        }
    suspend fun savePref(userName: String, radioButtonOption: RadioButtonOption) {
        context.dataStore.edit { preferences ->
            preferences[User] = userName
            preferences[q1] = radioButtonOption.name
        }
    }
}

class LogoPage : ComponentActivity() {
    private val userStore by lazy { UserStore(this) }

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
                    UserPreferencesDialog(userStore)
                }
            }
        }
    }
}


    // creating a class that takes the user's name and their answer and the question to ask



@Composable
fun UserPreferencesDialog(userStore: UserStore){
     //question1: String = stringResource(R.string.Question1)

//onSavePreferences:( preferences:UserPreferences) -> Unit

    val radioButtonOption = remember { mutableStateOf(RadioButtonOption.LessThanTen) }
    val (selectedOption,onOptionSelected) = remember { mutableStateOf(RadioButtonOption.entries.first()) }
    var userName by remember { mutableStateOf("") }
    val coroutine = rememberCoroutineScope()
    //val openDialog = remember { mutableStateOf(true) }
    val context = LocalContext.current
    val (data, setData) = remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6A3CBB)),
        //contentAlignment = Alignment.Center
    ) {

        AlertDialog(
            onDismissRequest = {},
            modifier = Modifier.padding(start = 30.dp, top = 50.dp, end = 30.dp, bottom = 50.dp)
                .size(570.dp)
                .width(940.dp)
                .height(650.dp),
            title = { Text(text = "User Preferences") },
            text = {
                Column {
                    Text(text = "Insert your name")
                    (Spacer(modifier = Modifier.height(10.dp)))
                    TextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("Name") },
                        //keyboardOptions =

                    )
                    Column {
                        (Spacer(modifier = Modifier.height(10.dp)))

                        Text(text = stringResource(R.string.Question1))

                        RadioButtonOption.entries.forEach { option ->
                            Row(verticalAlignment = Alignment.CenterVertically,
                            modifier= Modifier.selectableGroup()
                                .selectable(
                                    selected = (option == selectedOption),
                                    onClick = { onOptionSelected(option) },
                                    role = Role.RadioButton)){
                                RadioButton(
                                    selected = selectedOption == option,
                                    onClick = {onOptionSelected(option)}
                                )
                                Text(text = option.name)
                        }
                    }


                }}
            },
            confirmButton = {
                Button(
                    onClick = {
                        //getting the data gathered from user input and creating variables to store them
                        val selectedUserName = userName
                        val finalOption = selectedOption
                        //calling the read and write functions of the datastore to save and retrieve the data required
                        coroutine.launch{
                            withContext(Dispatchers.IO) {
                        userStore.savePref(selectedUserName, finalOption)}
                            val (name, option) = userStore.getPref.first()
                            setData("User Name: $name\nRadio Button Option: $option")
                            showDialog.value = true}
                        //openDialog.value = false
                    }
                ) {
                    Text(text = "Save")
                }
                if (showDialog.value) {
                    Dialog(onDismissRequest = { showDialog.value = false }) {
                        DisplayDataDialog(data = data,
                            onClose = { showDialog.value = false
                            context.startActivity(Intent(context, MainActivity::class.java))})
                    }
                }
            },

            )
    }
}


@Composable
fun DisplayDataDialog(data: String, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(text = "confirmation") },
        text = { Text(text = data) },
        confirmButton = {
            Button(onClick = onClose) {
                Text(text = "close")
            }
        }
    )
}



    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun GreetingPreview3() {
        WeatherAppTheme {
            val context= LocalContext.current
            UserPreferencesDialog(userStore = UserStore(context))
        }
    }


