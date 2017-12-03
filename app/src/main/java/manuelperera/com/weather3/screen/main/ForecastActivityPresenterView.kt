package manuelperera.com.weather3.screen.main

import android.support.v4.widget.SwipeRefreshLayout
import manuelperera.com.base.screen.presenter.PresenterView
import manuelperera.com.weather3.domain.objects.Forecast

interface ForecastActivityPresenterView: PresenterView {

    fun getSwipeRefreshLayout() : SwipeRefreshLayout

    fun initRecycler()

    fun getCurrentForecast()

    fun setToolbarInfo(forecast: Forecast)

    fun reloadAdapter()

    fun setNameOfTheCity(name: String)

}