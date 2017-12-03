package manuelperera.com.weather3.screen.main

import android.content.Context
import android.location.Geocoder
import manuelperera.com.base.screen.presenter.Presenter
import manuelperera.com.weather3.usecase.forecast.ClearCacheForecastUseCase
import manuelperera.com.weather3.usecase.forecast.GetForecastsUseCase
import manuelperera.com.weather3.usecase.forecast.SetLatAndLonUseCase
import java.util.*

class ForecastActivityPresenter(private val context: Context,
                                private val setLatAndLonUseCase: SetLatAndLonUseCase,
                                private val getForecastsUseCase: GetForecastsUseCase,
                                private val clearCacheForecastUseCase: ClearCacheForecastUseCase) : Presenter<ForecastActivityPresenterView>() {

    override fun init() {}

    fun setLatAndLng(lat: Double, lon: Double) {
        getNameOfCityByLatAndLon(lat, lon)
        addSubscription(setLatAndLonUseCase.bind(SetLatAndLonUseCase.Params(lat.toString(), lon.toString())).doOnComplete {
            view?.initRecycler()
        }.subscribe())
    }

    fun getCurrentForecast() {
        addSubscription(getForecastsUseCase.bind().subscribe { transaction ->
            if (transaction.isSuccess())
                view?.setToolbarInfo(transaction.data!![0])
        })
    }

    fun clearCacheForecast() {
        addSubscription(clearCacheForecastUseCase.bind().doOnComplete {
            view?.reloadAdapter()
        }.subscribe())
    }

    private fun getNameOfCityByLatAndLon(lat: Double, lon: Double) {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lon, 1)

        if (addresses.isNotEmpty())
            view?.setNameOfTheCity(addresses[0].locality)
    }

}