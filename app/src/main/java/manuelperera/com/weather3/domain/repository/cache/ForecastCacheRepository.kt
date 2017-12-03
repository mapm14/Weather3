package manuelperera.com.weather3.domain.repository.cache

import io.reactivex.Completable
import io.reactivex.Observable
import manuelperera.com.base.client.transaction.Transaction
import manuelperera.com.base.client.transaction.TransactionStatus
import manuelperera.com.weather3.domain.objects.ForecastByCity

open class ForecastCacheRepository {

    private var forecastByCity: ForecastByCity? = null

    open fun getForecasts(): Observable<Transaction<ForecastByCity>> =
            Observable.create { consumer ->
                forecastByCity?.let { forecast ->
                    consumer.onNext(Transaction(forecast, TransactionStatus.SUCCESS))
                }
                consumer.onComplete()
            }

    open fun cacheForecasts(forecast: ForecastByCity): Completable =
            Completable.create { consumer ->
                forecastByCity = forecast
                consumer.onComplete()
            }

    open fun clearCacheForecasts(): Completable =
            Completable.create { consumer ->
                forecastByCity = null
                consumer.onComplete()
            }

}