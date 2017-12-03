package manuelperera.com.weather3.screen.main.injection

import android.content.Context
import dagger.Module
import dagger.Provides
import manuelperera.com.weather3.screen.main.ForecastActivityPresenter
import manuelperera.com.weather3.usecase.forecast.ClearCacheForecastUseCase
import manuelperera.com.weather3.usecase.forecast.GetForecastsUseCase
import manuelperera.com.weather3.usecase.forecast.SetLatAndLonUseCase

@Module
class ForecastActivityModule {

    @Provides
    @ForecastActivityScope
    fun forecastActivityPresenter(context: Context,
                                  setLatAndLngUseCase: SetLatAndLonUseCase,
                                  getForecastsUseCase: GetForecastsUseCase,
                                  clearCacheForecastUseCase: ClearCacheForecastUseCase): ForecastActivityPresenter =
            ForecastActivityPresenter(context, setLatAndLngUseCase, getForecastsUseCase, clearCacheForecastUseCase)

}