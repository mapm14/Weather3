package manuelperera.com.weather3.usecase.forecast

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import manuelperera.com.base.usecase.UseCase
import manuelperera.com.weather3.domain.service.ForecastService

class ClearCacheForecastUseCase(private val forecastService: ForecastService) : UseCase<Completable> {

    override fun bind(): Completable =
            forecastService.clearCacheForecast().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

}