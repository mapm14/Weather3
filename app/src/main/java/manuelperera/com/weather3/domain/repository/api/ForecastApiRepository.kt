package manuelperera.com.weather3.domain.repository.api

import io.reactivex.Observable
import manuelperera.com.base.client.transaction.Transaction
import manuelperera.com.base.client.transaction.TransactionRequestFactory
import manuelperera.com.weather3.client.apiclient.ForecastApiClient
import manuelperera.com.weather3.domain.objects.ForecastByCity

open class ForecastApiRepository(private val forecastApiClient: ForecastApiClient,
                                 private val forecastByCityTransactionRequestFactory: TransactionRequestFactory<ForecastByCity>) {

    open fun getForecasts(lat: String, lon: String): Observable<Transaction<ForecastByCity>> =
            forecastByCityTransactionRequestFactory.createTransactionRequest().modifyObservable(forecastApiClient.getForecasts(lat, lon))

}