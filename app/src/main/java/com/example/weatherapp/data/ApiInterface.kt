import com.example.weatherapp.data.models.Coord
import com.example.weatherapp.data.models.CurrentCoord
import com.example.weatherapp.data.models.CurrentWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Response<CurrentWeather>

    @GET("data/2.5/weather")
    suspend fun getCurrentCoord(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): Response<CurrentWeather>
}

