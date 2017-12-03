package manuelperera.com.weather3.client.retrofit

import io.reactivex.Observable
import manuelperera.com.weather3.domain.objects.ForecastByCity
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitForecastApiClient {

    @GET("data/2.5/forecast")
    fun getForecasts(@Query("lat") lat: String, @Query("lon") lon: String,
                     @Query("units") units: String = "metric", @Query("lang") lang: String = "en"): Observable<Result<ForecastByCity>>

}