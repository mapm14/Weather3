package manuelperera.com.weather3.usecase.forecast

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import manuelperera.com.base.usecase.UseCaseParams
import manuelperera.com.base.usecase.UseCaseWithParams
import manuelperera.com.weather3.domain.service.ForecastService

class SetLatAndLonUseCase(private val forecastService: ForecastService) : UseCaseWithParams<Completable, SetLatAndLonUseCase.Params> {

    override fun bind(params: Params): Completable =
            forecastService.setLatAndLon(params.lat, params.lon).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    class Params(val lat: String, val lon: String) : UseCaseParams()

}