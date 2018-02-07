package manuelperera.com.weather3.domain.service

import io.reactivex.Completable
import io.reactivex.Observable
import manuelperera.com.base.client.transaction.Transaction
import manuelperera.com.weather3.domain.model.ForecastModel
import manuelperera.com.weather3.domain.objects.ForecastByCity

open class ForecastService(private val forecastModel: ForecastModel) {

    private lateinit var lat: String
    private lateinit var lon: String

    open fun setLatAndLon(lat: String, lon: String): Completable =
            Completable.create {
                this.lat = lat
                this.lon = lon
                it.onComplete()
            }

    open fun getForecasts(): Observable<Transaction<ForecastByCity>> =
            forecastModel.getForecasts(lat, lon)

    open fun clearCacheForecast(): Completable =
            forecastModel.clearCacheForecast()

}