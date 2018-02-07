package manuelperera.com.weather3.domain.model

import io.reactivex.Completable
import io.reactivex.Observable
import manuelperera.com.base.client.transaction.Transaction
import manuelperera.com.base.client.transaction.TransactionStatus
import manuelperera.com.weather3.domain.objects.ForecastByCity
import manuelperera.com.weather3.domain.repository.api.ForecastApiRepository
import manuelperera.com.weather3.domain.repository.cache.ForecastCacheRepository

open class ForecastModel(private val forecastApiRepository: ForecastApiRepository,
                         private val forecastCacheRepository: ForecastCacheRepository) {

    open fun getForecasts(lat: String, lon: String): Observable<Transaction<ForecastByCity>> =
            forecastCacheRepository.getForecasts()
                    .concatWith(forecastApiRepository.getForecasts(lat, lon))
                    .doOnNext { transaction ->
                        transaction.data?.let { data ->
                            if (transaction.isSuccess())
                                forecastCacheRepository.cacheForecasts(data)
                        }
                    }
                    .first(Transaction(status = TransactionStatus.EXCEPTION)).toObservable()

    open fun clearCacheForecast(): Completable =
            forecastCacheRepository.clearCacheForecasts()

}