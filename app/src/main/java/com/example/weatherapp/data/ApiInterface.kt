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
}
