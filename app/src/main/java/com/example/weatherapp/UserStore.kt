package com.example.weatherapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class RadioButtonOption {
    LessThanTwenty,
    LessThanTen,
    LessThanFive
}

class UserStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userPref")
        private val User = stringPreferencesKey("user_name")
        private val q1 = stringPreferencesKey("radio_button_option")
    }

    // Read operation
    val getPref: Flow<Pair<String, RadioButtonOption>> = context.dataStore.data
        .map { preferences ->
            val radioButtonOption = RadioButtonOption.valueOf(preferences[Companion.q1] ?: RadioButtonOption.LessThanTen.name)
            val userName = preferences[Companion.User] ?: ""
            Pair(userName, radioButtonOption)
        }

    suspend fun savePref(userName: String, radioButtonOption: RadioButtonOption) {
        context.dataStore.edit { preferences ->
            preferences[Companion.User] = userName
            preferences[Companion.q1] = radioButtonOption.name
        }
    }
}
