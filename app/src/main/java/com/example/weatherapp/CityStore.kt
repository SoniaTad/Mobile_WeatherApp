package com.example.weatherapp

import android.content.Context
import com.example.weatherapp.data.models.CurrentWeather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CityStore(private val context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("CityStore", Context.MODE_PRIVATE)

    // Flow to emit changes in the list of cities
    private val citiesFlow: MutableStateFlow<List<CurrentWeather>> =
        MutableStateFlow(getCities())
    // Function to collect changes in the list of cities
    fun observeCities(): StateFlow<List<CurrentWeather>> = citiesFlow

    fun saveCity(weather: CurrentWeather) {
        val cities = getCities().toMutableList()
        cities.add(weather)
        sharedPreferences.edit().putString("cities", Gson().toJson(cities)).apply()
        citiesFlow.value = cities.toList()
    }

    fun getCities(): List<CurrentWeather> {
        val citiesJson = sharedPreferences.getString("cities", null)
        return Gson().fromJson(citiesJson, object : TypeToken<List<CurrentWeather>>() {}.type)
            ?: emptyList()
    }

    fun removeCity(cityName: String) {
        val cities = getCities().toMutableList()
        cities.removeAll { it.name == cityName }
        sharedPreferences.edit().putString("cities", Gson().toJson(cities)).apply()
        citiesFlow.value = cities.toList()
    }

    // Add a function to clear all cities
    fun clearCities() {
        sharedPreferences.edit().remove("cities").apply()
    }
}
