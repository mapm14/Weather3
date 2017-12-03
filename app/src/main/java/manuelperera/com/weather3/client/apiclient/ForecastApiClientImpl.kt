package manuelperera.com.weather3.client.apiclient

import io.reactivex.Observable
import manuelperera.com.weather3.client.retrofit.RetrofitForecastApiClient
import manuelperera.com.weather3.domain.objects.ForecastByCity
import retrofit2.adapter.rxjava2.Result

class ForecastApiClientImpl(private val retrofitForecastApiClient: RetrofitForecastApiClient) : ForecastApiClient {

    override fun getForecasts(lat: String, lon: String): Observable<Result<ForecastByCity>> =
            retrofitForecastApiClient.getForecasts(lat, lon)

}