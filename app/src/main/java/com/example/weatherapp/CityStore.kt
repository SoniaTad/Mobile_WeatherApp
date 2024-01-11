
package com.example.weatherapp
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CityStore(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("CityStore", Context.MODE_PRIVATE)
    private val gson = Gson()


    fun getFavoriteCities(): Set<String> {
        val citiesJson = sharedPreferences.getString("favoriteCities", null)
        return if (citiesJson != null) {
            gson.fromJson(citiesJson, object : TypeToken<Set<String>>() {}.type)
        } else {
            emptySet()
        }
    }


    fun saveCity(cityName: String) {
        val currentCities = getFavoriteCities().toMutableSet()
        currentCities.add(cityName)
        val citiesJson = gson.toJson(currentCities)
        sharedPreferences.edit().putString("favoriteCities", citiesJson).apply()
    }


    fun removeCity(cityName: String) {
        val currentCities = getFavoriteCities().toMutableSet()
        currentCities.remove(cityName)
        val citiesJson = gson.toJson(currentCities)
        sharedPreferences.edit().putString("favoriteCities", citiesJson).apply()
    }
}

