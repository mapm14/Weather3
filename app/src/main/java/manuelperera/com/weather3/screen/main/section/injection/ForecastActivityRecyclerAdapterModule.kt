package manuelperera.com.weather3.screen.main.section.injection

import dagger.Module
import dagger.Provides
import manuelperera.com.weather3.screen.main.section.ForecastActivityRecyclerAdapterPresenter
import manuelperera.com.weather3.usecase.forecast.GetForecastsUseCase

@Module
class ForecastActivityRecyclerAdapterModule {

    @Provides
    @ForecastActivityRecyclerAdapterScope
    fun forecastActivityRecyclerPresenter(getForecastsUseCase: GetForecastsUseCase): ForecastActivityRecyclerAdapterPresenter =
            ForecastActivityRecyclerAdapterPresenter(getForecastsUseCase)

}