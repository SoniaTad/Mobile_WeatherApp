import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("LocationData")
        private val LOCATION_ENABLED = booleanPreferencesKey("location_enabled")
        private val LATITUDE = doublePreferencesKey("latitude")
        private val LONGITUDE = doublePreferencesKey("longitude")
    }

    // Read operation
    val getLocation: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            val enabled = preferences[Companion.LOCATION_ENABLED] ?: false
            enabled
        }


    val getLatitude: Flow<Double> = context.dataStore.data
        .map { preferences ->
            val latitude = preferences[Companion.LATITUDE] ?: 0.0
            latitude
        }


    val getLongitude: Flow<Double> = context.dataStore.data
        .map { preferences ->
            val longitude = preferences[Companion.LONGITUDE] ?: 0.0
            longitude
        }



    suspend fun saveLoc(context: Context, enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LocationStore.LOCATION_ENABLED] = enabled

        }
    }

    suspend fun saveDetails(context: Context, latitude: Double, longitude: Double) {
        context.dataStore.edit { preferences ->
            preferences[LocationStore.LATITUDE] = latitude
            preferences[LocationStore.LONGITUDE] = longitude
        }
    }
}