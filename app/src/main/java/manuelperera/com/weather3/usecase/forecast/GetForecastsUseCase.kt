package manuelperera.com.weather3.usecase.forecast

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import manuelperera.com.base.client.transaction.Transaction
import manuelperera.com.base.usecase.UseCase
import manuelperera.com.weather3.domain.objects.Forecast
import manuelperera.com.weather3.domain.service.ForecastService

class GetForecastsUseCase(private val forecastService: ForecastService) : UseCase<Observable<Transaction<List<Forecast>>>> {

    override fun bind(): Observable<Transaction<List<Forecast>>> =
            forecastService.getForecasts().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map { transaction ->
                Transaction(transaction.data?.forecasts, transaction.status)
            }

}