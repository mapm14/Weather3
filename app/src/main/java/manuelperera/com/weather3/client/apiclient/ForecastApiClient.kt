package manuelperera.com.weather3.client.apiclient

import io.reactivex.Observable
import manuelperera.com.weather3.domain.objects.ForecastByCity
import retrofit2.adapter.rxjava2.Result

interface ForecastApiClient {

    fun getForecasts(lat: String, lon: String): Observable<Result<ForecastByCity>>

}