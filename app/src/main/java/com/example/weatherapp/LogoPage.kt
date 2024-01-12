package com.example.weatherapp

import android.annotation.SuppressLint

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

import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.first

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext




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






@SuppressLint("UnrememberedMutableState")
@Composable
fun UserPreferencesDialog(userStore: UserStore){


    val radioButtonOption = remember { mutableStateOf(RadioButtonOption.LessThanTen) }
    val (selectedOption,onOptionSelected) = remember { mutableStateOf(RadioButtonOption.entries.first()) }
    var userName by remember { mutableStateOf("") }
    val coroutine = rememberCoroutineScope()
    //val openDialog = remember { mutableStateOf(true) }
    var inputError by mutableStateOf(false)

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
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(
                        value = userName,
                        onValueChange = { input ->
                            userName = input
                            inputError = input.isBlank() // Example validation: check if the input is blank
                        },
                        label = { Text("Name") },
                    )

                    if (inputError) {
                        Text(
                            text = "Please enter a valid name",
                            color = Color.Red,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
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
                        val selectedUserName: String
                        //getting the data gathered from user input and creating variables to store them
                        //checking that userName is not null
                        if(userName.isBlank()){ selectedUserName="User"}
                        else{
                         selectedUserName = userName}
                        val finalOption = selectedOption

                        //calling the read and write functions of the datastore to save and retrieve the data required
                        coroutine.launch{
                            withContext(Dispatchers.IO) {
                        userStore.savePref(selectedUserName, finalOption)}
                            val (name, option) = userStore.getPref.first()
                            setData("User Name: $name\nRadio Button Option: $option")
                            showDialog.value = true}
                    }
                ) {
                    Text(text = "Save")
                }
                if (showDialog.value) {
                    Dialog(onDismissRequest = { showDialog.value = false }) {
                        DisplayDataDialog(data = data,
                            onClose = {
                                showDialog.value = false
                            //context.startActivity(Intent(context, MainActivity::class.java))
                                // this is to send user pref to the settings activity
                                val firstIntent = Intent(context, Settings::class.java)

                                context.startActivity(firstIntent)



                            })
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


